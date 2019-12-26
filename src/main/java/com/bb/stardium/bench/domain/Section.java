package com.bb.stardium.bench.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Section {
    JONGRO("종로구"),
    JUNGGU("중구"),
    YONGSAN("용산구"),
    SUNGDONG("성동구"),
    GWANGJIN("광진구"),
    DONGDAEMUN("동대문구"),
    JUNGRANG("중랑구"),
    SUNGBUK("성북구"),
    GANGBOOK("강북구"),
    DOBONGGU("도봉구"),
    NOWON("노원구"),
    EUNPYEONG("은평구"),
    SEODAEMUN("서대문구"),
    MAPO("마포구"),
    YANGCHEON("양천구"),
    GANGSEO("강서구"),
    GURO("구로구"),
    GEUMCHEON("금천구"),
    YEONGDEUNGPO("영등포구"),
    DONGJAK("동작구"),
    GWANAK("관악구"),
    SEOCHO("서초구"),
    GANGNAM("강남구"),
    SONGPA("송파구"),
    GANGDONG("강동구");

    private String sectionName;

    Section(String sectionName) {
        this.sectionName = sectionName;
    }

    public static List<String> getAllSections() {
        return Arrays.stream(values())
                .map(Section::getSectionName)
                .sorted()
                .collect(Collectors.toList());
    }

    private String getSectionName() {
        return this.sectionName;
    }
}
