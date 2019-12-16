package com.bb.stardium.mediafile.service;

import com.bb.stardium.mediafile.config.MediaFileResourceLocation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MediaFileService {
    private static final Logger log = LoggerFactory.getLogger(MediaFileService.class);

    private final MediaFileResourceLocation mediaFileResourceLocation;

    public String save(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        File dest = new File(mediaFileResourceLocation.getLocation() + fileName);
        try {
            file.transferTo(dest);
            return mediaFileResourceLocation.getUrl() + fileName;
        } catch (IOException e) {
            throw new IllegalStateException("Fail To handle file");
        }
    }
}
