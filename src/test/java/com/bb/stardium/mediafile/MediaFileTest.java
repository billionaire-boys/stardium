package com.bb.stardium.mediafile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MediaFileTest {
    @Test
    @DisplayName("공백 url은 기본 URL로 변")
    void vaidUrl() {
        MediaFile mediaFile = new MediaFile("");
        assertThat(mediaFile.getUrl()).isNotEqualTo("");
    }
}