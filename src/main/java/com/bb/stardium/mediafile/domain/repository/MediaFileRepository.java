package com.bb.stardium.mediafile.domain.repository;

import com.bb.stardium.mediafile.domain.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
}
