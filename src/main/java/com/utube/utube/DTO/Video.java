package com.utube.utube.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {

    private String formatId; // Unique format code (from yt-dlp/YouTube)
    private String resolution; // Resolution (e.g. 360p, 720p, 1080p, audio-only)
    private String quality; // Quality label (e.g. "hd720", "medium", "audio")
    private long fileSize; // Estimated file size in bytes
    private String downloadUrl; // Direct download URL (optional, generated later)
    private String ext;
}
