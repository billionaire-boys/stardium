package com.bb.stardium.mediafile.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@Entity
public class MediaFile {
    private static final String DEFAULT_URL = "/images/profile-default.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String url;

    public MediaFile(String url) {
        this.url = validUrl(url);
    }

    public MediaFile update(MediaFile update) {
        this.url = update.url;
        return this;
    }

    private String validUrl(String inputUrl) {
        if (inputUrl == null || inputUrl.isEmpty()) {
            return DEFAULT_URL;
        }
        return inputUrl;
    }
}
