package com.bb.stardium.mediafile.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MediaFileResourceLocation {
    @Value("${img.location}")
    private String location;

    @Value("${img.url}")
    private String url;
}
