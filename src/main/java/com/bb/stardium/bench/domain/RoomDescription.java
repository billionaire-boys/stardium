package com.bb.stardium.bench.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class RoomDescription {
    @Column(name = "title", length = 100)
    private String title;

    @Lob
    private String intro;
    private int playerLimit;
}
