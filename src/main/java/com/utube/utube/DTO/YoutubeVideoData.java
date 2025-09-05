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

    private String title;
    private String thumbnail;
    private String channel;
    private String channelId;
    private String duration;
    private long views;
    private String videoId;
    private String audioId;
    private Audio audio;
    private List<Video> videoFormats = new ArrayList<>();

    public static String formatViews(long views) {
        if (views >= 1_000_000_000) {
            return String.format("%,.1f Billion", views / 1_000_000_000.0);
        } else if (views >= 1_000_000) {
            return String.format("%,.1f Million", views / 1_000_000.0);
        } else if (views >= 1_000) {
            return String.format("%,d", views);
        } else {
            return String.valueOf(views);
        }
    }

}
