package com.musicapp.musicBE.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class SongStreamingService {

    private static final long CHUNK_SIZE = 1024 * 1024L; // 1 MB chunks

    public ResponseEntity<ResourceRegion> streamAudio(String filePath, String rangeHeader) throws IOException {
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            // Fallback to a default sample if specified file not found
            resource = new FileSystemResource("data/audio/sample.mp3");
            if (!resource.exists()) {
                throw new IOException("Audio file not found: " + filePath);
            }
        }

        long contentLength = resource.contentLength();
        ResourceRegion region = getResourceRegion(resource, rangeHeader, contentLength);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header("Accept-Ranges", "bytes")
                .body(region);
    }

    private ResourceRegion getResourceRegion(Resource resource, String rangeHeader, long contentLength) {
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : contentLength - 1;

            if (end >= contentLength) {
                end = contentLength - 1;
            }

            long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
            return new ResourceRegion(resource, start, rangeLength);
        } else {
            long rangeLength = Math.min(CHUNK_SIZE, contentLength);
            return new ResourceRegion(resource, 0, rangeLength);
        }
    }
}

