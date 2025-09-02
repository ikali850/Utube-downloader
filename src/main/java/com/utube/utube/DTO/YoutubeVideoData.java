package com.utube.utube.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YoutubeVideoData {

    private String title; // Video title
    private String thumbnail; // Thumbnail URL
    private String channel; // Channel name
    private String channelId; // Channel ID (optional but useful)
    private String duration; // Duration in HH:MM:SS format
    private long views; // Views count
    private String videoId; // YouTube video ID
    private String audioId; // Best audio format ID
    private Audio audio;
    private List<Video> videoFormats = new ArrayList<>();

}
