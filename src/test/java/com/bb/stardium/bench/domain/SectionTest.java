package com.bb.stardium.bench.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {
    @Test
    void 서울_전체_() {
        List<String> allSections = Section.getAllSections();
        assertThat(allSections).isNotNull();
        assertThat(allSections.size()).isEqualTo(25);
    }
}