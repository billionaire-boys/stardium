package com.bb.stardium.mediafile.domain.repository;

import com.bb.stardium.mediafile.domain.MediaFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MediaFileRepositoryTest {
    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Test
    @DisplayName("미디어파일 CRUD")
    void findMediaFile() {
        MediaFile mediaFile = new MediaFile("");
        mediaFileRepository.save(mediaFile);

        assertThat(mediaFile.getId()).isNotNull();
        assertThat(mediaFile.getUrl()).isNotNull();

        mediaFileRepository.delete(mediaFile);
        Long id = mediaFile.getId();

        assertThat(mediaFileRepository.findById(id).isEmpty()).isTrue();
    }
}