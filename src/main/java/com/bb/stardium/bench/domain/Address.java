package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.domain.exception.NotAllowCityException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void setCity(String city) {
        this.city = checkCityName(city);
    }

    private String checkCityName(String city) {
        if (!city.contains("서울")) {
            throw new NotAllowCityException();
        }
        return city;
    }
}
