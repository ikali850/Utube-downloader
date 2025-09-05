package com.utube.utube.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.utube.utube.DTO.Video;
import com.utube.utube.DTO.YoutubeVideoData;
import com.utube.utube.service.YoutubeService;

import jakarta.servlet.http.HttpSession;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HomeController {

    @Autowired
    private YoutubeService youtubeService;

    private final String FILE_PATH = "/home/arvind/yt-dlp";

    @GetMapping("/")
    public ModelAndView getHomePage() {
        return new ModelAndView("index.html");
    }

    @GetMapping("/convert")
    public ModelAndView getVideoInfo(@RequestParam String url, HttpSession httpSession) {

        YoutubeVideoData youtubeVideoData = new YoutubeVideoData();

        // get video info
        try {
            JsonNode videoData = this.youtubeService.getVideoInfo(url);
            youtubeVideoData = this.youtubeService.parseVideo(videoData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("result.html");
        mv.addObject("videoInfo", youtubeVideoData);
        httpSession.setAttribute("videoData", youtubeVideoData);
        return mv;
    }

    @GetMapping("/premium/download/video/{formatId}")
    public ResponseEntity<StreamingResponseBody> premiumVideoDownload(
            @PathVariable String formatId, HttpSession httpSession) throws IOException, InterruptedException {
        YoutubeVideoData youtubeVideoData = (YoutubeVideoData) httpSession.getAttribute("videoData");
        // Generate a local filename (unique per videoId + formatId)
        String fileName = youtubeVideoData.getTitle().replace(" ", "-") + ".mp4";
        String videoId = youtubeVideoData.getVideoId();
        String audioId = youtubeVideoData.getAudio().getFormatId();
        Path filePath = Paths.get(FILE_PATH, fileName);
        // If file not already downloaded, fetch using yt-dlp
        // if (!Files.exists(filePath)) {
        // String cmd = String.format(
        // "yt-dlp -f %s+%s --merge-output-format mp4 -o '%s'
        // https://www.youtube.com/watch?v=%s",
        // formatId, audioId, filePath.toAbsolutePath(), videoId);
        // ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        // pb.redirectErrorStream(false);
        // Process process = pb.start();
        // process.waitFor();
        // }
        System.out.println("premius page accessed");
        if (true) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(null);
        }
        if (!Files.exists(filePath)) {
            System.out.println("file not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // Stream file and delete afterwards
        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                inputStream.transferTo(outputStream);
            } finally {
                Files.deleteIfExists(filePath);
            }
        };
        // Build proper Content-Disposition header
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(fileName)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(filePath))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    @GetMapping("/download/video/{formatId}")
    public ResponseEntity<StreamingResponseBody> downloadVideo(
            @PathVariable String formatId, HttpSession httpSession) throws IOException, InterruptedException {
        YoutubeVideoData youtubeVideoData = (YoutubeVideoData) httpSession.getAttribute("videoData");
        // Generate a local filename (unique per videoId + formatId)
        String fileName = youtubeVideoData.getTitle().replace(" ", "-") + ".mp4";
        String videoId = youtubeVideoData.getVideoId();
        String audioId = youtubeVideoData.getAudio().getFormatId();
        Path filePath = Paths.get(FILE_PATH, fileName);
        // If file not already downloaded, fetch using yt-dlp
        if (!Files.exists(filePath)) {
            String cmd = String.format(
                    "yt-dlp -f %s+%s --merge-output-format mp4 -o '%s' https://www.youtube.com/watch?v=%s",
                    formatId, audioId, filePath.toAbsolutePath(), videoId);

            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(false);
            Process process = pb.start();
            process.waitFor();
        }

        if (!Files.exists(filePath)) {
            System.out.println("file not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // Stream file and delete afterwards
        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                inputStream.transferTo(outputStream);
            } finally {
                Files.deleteIfExists(filePath);
            }
        };
        // Build proper Content-Disposition header
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(fileName)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(filePath))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    @GetMapping("/download/audio/{videoId}")
    public ResponseEntity<StreamingResponseBody> downloadAudio(
            @PathVariable String videoId,
            HttpSession httpSession) throws IOException, InterruptedException {
        YoutubeVideoData youtubeVideoData = (YoutubeVideoData) httpSession.getAttribute("videoData");

        String fileName = youtubeVideoData.getTitle().replace(" ", "-") + ".mp3";
        Path filePath = Paths.get(FILE_PATH, fileName);

        // Download if not already exists
        if (!Files.exists(filePath)) {
            String cmd = String.format(
                    "yt-dlp -f bestaudio --extract-audio --audio-format mp3 -o '%s' https://www.youtube.com/watch?v=%s",
                    filePath.toAbsolutePath(), videoId);

            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(false);
            Process process = pb.start();
            process.waitFor();
        }

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Stream file and delete afterwards
        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                inputStream.transferTo(outputStream);
            } finally {
                Files.deleteIfExists(filePath);
            }
        };

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(fileName)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(filePath))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

}
