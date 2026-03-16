package com.face.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Value("${face.upload-path}")
    private String uploadPath;

    @GetMapping("/play")
    public ResponseEntity<Resource> playVideo(@RequestParam("file") String fileName) {
        Path videoPath = Paths.get(uploadPath, fileName);
        FileSystemResource resource = new FileSystemResource(videoPath);
        
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4")) // Assuming MP4, or determine dynamically
                .body(resource);
    }
}
