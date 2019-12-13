package com.bb.stardium.oauth.web;

import com.bb.stardium.oauth.dto.KakaoAccessTokenDto;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
@RequestMapping("/oauth")
public class OauthController {

    @GetMapping("/kakao")
    public String getToken(@RequestParam("code") String code, Model model) {
        Mono<ResponseEntity<KakaoAccessTokenDto>> responseEntityMono = WebClient.create("https://kauth.kakao.com")
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", "974d268d773b35a4a9e63eca0b74ec16")
                        .queryParam("redirect_uri", "http://15.164.95.121:8080/oauth/kakao")
                        .queryParam("code", code).build())
                .retrieve()
                .toEntity(KakaoAccessTokenDto.class);

        Mono<String> authorization = WebClient.create("https://kapi.kakao.com")
                .get()
                .uri(uriBuilder -> uriBuilder.path("/v2/user/me").build())
                .header("Authorization", "Bearer " + responseEntityMono.block().getBody().getAccessToken())
                .retrieve()
                .bodyToMono(String.class);

        Map<String, Object> map = new JacksonJsonParser().parseMap(authorization.block());
        String email = ((Map) map.get("kakao_account")).get("email").toString();
        String nick = ((Map) map.get("properties")).get("nickname").toString();
        model.addAttribute("email", email);
        model.addAttribute("nickname", nick);
        return "signup.html";
    }
}
