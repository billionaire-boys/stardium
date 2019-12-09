package com.bb.stardium.player.dto;

import com.bb.stardium.mediafile.MediaFile;
import com.bb.stardium.player.domain.Player2;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Player2RequestDto {
    private String nickname;
    private String email;
    private String password;
    private String statusMessage;
    private String profile;

    public Player2 toEntity() {
        return Player2.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .statusMessage(statusMessage)
                .profile(new MediaFile(password))
                .build();
    }
}
