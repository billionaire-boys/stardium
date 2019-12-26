package com.bb.stardium.mediafile.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
public class MediaFile {
    private static final String DEFAULT_URL = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Favatars2.githubusercontent.com%2Fu%2F41888327%3Fs%3D460%26v%3D4&type=b400";

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
