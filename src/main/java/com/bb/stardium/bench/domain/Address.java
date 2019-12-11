package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.domain.exception.NotAllowCityException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Address {
    private static final Logger log = LoggerFactory.getLogger(Address.class);

    @NotBlank(message = "시를 적어주세요.")
    private String city;

    @NotBlank(message = "구를 적어주세요.")
    private String section;

    @NotBlank(message = "자세한 주소를 적어주세요.")
    private String detail;

    @Builder
    public Address(@NotBlank(message = "시를 적어주세요.") String city, @NotBlank(message = "구를 적어주세요.") String section, @NotBlank(message = "자세한 주소를 적어주세요.") String detail) {
        this.city = city;
        this.section = section;
        this.detail = detail;
    }

    public void setCity(String city) {
        this.city = checkCityName(city);
    }

    private String checkCityName(String city) {
        if (!city.contains("서울")) {
            throw new NotAllowCityException("서울시만 가능합니다.");
        }
        return city;
    }
}
