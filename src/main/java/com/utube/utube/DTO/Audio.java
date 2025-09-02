package com.utube.utube.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Audio {
    private String formatId; // Unique format code (yt-dlp audio format ID)
    private String extension; // File extension (e.g. mp3, m4a, webm)
    private long fileSize; // Estimated file size in bytes
    private String downloadUrl; // Direct download URL (optional, generated later)
}
