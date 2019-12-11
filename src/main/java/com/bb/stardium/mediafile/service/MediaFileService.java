package com.bb.stardium.mediafile.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class MediaFileService {
    private static final Logger log = LoggerFactory.getLogger(MediaFileService.class);

    public String save(String path, MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        File dest = new File(path + fileName);
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new IllegalStateException("Fail To handle file");
        }
    }
}
