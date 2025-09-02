package com.utube.utube.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utube.utube.DTO.Audio;
import com.utube.utube.DTO.Video;
import com.utube.utube.DTO.YoutubeVideoData;

@Service
public class YoutubeService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String FILE_PATH = "/TMP/VIDEO/";

    public JsonNode getVideoInfo(String url) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "-J", // dump JSON
                "-f", "all", // request ALL formats
                "--no-warnings",
                url);

        pb.redirectErrorStream(false);

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("yt-dlp failed to fetch video info");
        }

        return objectMapper.readTree(output.toString());
    }

    public YoutubeVideoData parseVideo(JsonNode info) {
        YoutubeVideoData dto = new YoutubeVideoData();
        dto.setTitle(info.get("title").asText());
        dto.setThumbnail(info.get("thumbnail").asText());
        dto.setChannel(info.get("uploader").asText());
        dto.setViews(info.get("view_count").asLong());
        dto.setVideoId(info.path("id").asText(null));
        // Convert duration (seconds -> mm:ss)
        int durationSec = info.get("duration").asInt();
        String duration = String.format("%02d:%02d", durationSec / 60, durationSec % 60);
        dto.setDuration(duration);
        List<Video> videos = new ArrayList<>();

        // To avoid duplicate resolutions
        Set<String> seenResolutions = new HashSet<>();

        for (JsonNode f : info.get("formats")) {
            String formatId = f.get("format_id").asText();
            String ext = f.get("ext").asText();

            Long filesize = null;
            if (f.has("filesize") && !f.get("filesize").isNull()) {
                filesize = f.get("filesize").asLong();
            } else if (f.has("filesize_approx") && !f.get("filesize_approx").isNull()) {
                filesize = f.get("filesize_approx").asLong();
            }

            // Audio-only
            boolean isAudio = f.has("acodec") && !f.get("acodec").asText("").equals("none") &&
                    f.has("vcodec") && f.get("vcodec").asText("none").equals("none");

            // Video (has vcodec)
            boolean isVideo = f.has("vcodec") && !f.get("vcodec").asText("").equals("none");

            if (isAudio) {
                int abr = f.has("abr") && !f.get("abr").isNull() ? f.get("abr").asInt() : 0;
                Audio audioFormat = Audio.builder()
                        .fileSize(filesize)
                        .extension(ext)
                        .formatId(formatId)
                        .downloadUrl("/download/audio/" + dto.getVideoId())
                        .build();
                dto.setAudio(audioFormat);
            }

            if (isVideo) {
                int height = f.has("height") && !f.get("height").isNull() ? f.get("height").asInt() : 0;
                int tbr = f.has("tbr") && !f.get("tbr").isNull() ? f.get("tbr").asInt() : 0;

                String resolution = height > 0 ? height + "p"
                        : (f.has("format_note") ? f.get("format_note").asText() : "");

                // Skip duplicates (only keep first format for each resolution)
                if (seenResolutions.contains(resolution)) {
                    continue;
                }
                seenResolutions.add(resolution);

                Video video = new Video();
                video.setFileSize(filesize);
                video.setFormatId(formatId);
                video.setQuality(resolution);
                video.setExt(ext);
                if (filesize > 1024L * 1024L * 1024L) { // > 1GB
                    video.setDownloadUrl("/premium/download/video/" + formatId);
                } else {
                    video.setDownloadUrl("/download/video/" + formatId);
                }

                videos.add(video);
            }
        }
        dto.setVideoFormats(videos);
        return dto;
    }

}
