package com.bb.stardium.oauth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.json.JacksonJsonParser;

import java.util.Map;

class KakaoAccessTokenDtoTest {

    @Test
    void name() {
        String json = " {\"id\":1234476852,\"properties\":{\"nickname\":\"JYK\"},\"kakao_account\":{\"profile_needs_agreement\":false,\"profile\":{\"nickname\":\"JYK\"},\"has_email\":true,\"email_needs_agreement\":false,\"is_email_valid\":true,\"is_email_verified\":true,\"email\":\"andole98@naver.com\"}}";

        Map<String, Object> stringObjectMap = new JacksonJsonParser().parseMap(json);

        stringObjectMap.get("kakao_account");
    }
}