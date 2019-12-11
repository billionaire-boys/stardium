package com.bb.stardium.mediafile.config;

import lombok.Getter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Getter
public class MediaFileResourceLocation {
    @Value("${img.location}")
    private String location;

    public MediaFileResourceLocation(String location) {
        this.location = location;
        if (!createLocation(location)) {
            throw new BeanCreationException("미디어파일 경로설정에 실패했습니다.");
        }
    }

    private boolean createLocation(String location) {
        File file = new File(location);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }
}
