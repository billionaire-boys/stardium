package com.bb.stardium.common.web.argumentresolver;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Redirection {
    private String redirectUrl;

    Redirection(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
