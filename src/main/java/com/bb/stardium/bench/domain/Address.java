package com.bb.stardium.bench.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class Address {
    @NotBlank
    private String city;

    @NotBlank
    private String section; // TODO : 지역를 영어로 뭐라고 하지?

    @NotBlank
    private String detail;
}
