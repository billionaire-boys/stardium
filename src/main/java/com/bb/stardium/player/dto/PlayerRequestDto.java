package com.bb.stardium.player.dto;

import com.bb.stardium.common.util.EscapedCharacters;
import com.bb.stardium.mediafile.domain.MediaFile;
import com.bb.stardium.player.domain.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerRequestDto {
    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;
    private String statusMessage;
    private String mediaFile;

    @Builder
    public PlayerRequestDto(String nickname, String email, String password, String confirmPassword, String statusMessage) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.statusMessage = statusMessage;
    }

    public Player toEntity() {
        return Player.builder()
                .nickname(EscapedCharacters.of(nickname))
                .email(EscapedCharacters.of(email))
                .password(password)
                .statusMessage(EscapedCharacters.of(statusMessage))
                .profile(new MediaFile(mediaFile))
                .build();
    }

    public boolean isEqualPassword() {
        return this.password.equals(this.confirmPassword);
    }
}
