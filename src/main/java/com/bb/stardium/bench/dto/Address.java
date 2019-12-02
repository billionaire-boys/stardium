package com.bb.stardium.bench.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Address {
    @NotBlank
    private String city;

    @NotBlank
    private String gu; // TODO : gu를 영어로 뭐라고 하지?

    @NotBlank
    private String detail;
}
