package com.bb.stardium.player.repository;

import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
class PlayerRepositoryTest {
    private final String email = "email";
    private final Player samplePlayer = new Player("nickname", email, "password");

    @Autowired
    PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일로 플레이어 1명 찾기")
    void findByEmail() {
        final Player player = playerRepository.save(samplePlayer);
        final Player actual = playerRepository.findByEmail(email).get();
        assertThat(player).isEqualTo(actual);
    }

    @Test
    @DisplayName("없는 플레이어 찾기")
    void findNotExistPlayerByEmail() {
        assertThat(playerRepository.findByEmail("doesn't exist player").isEmpty()).isTrue();
    }

    @Test
    @DisplayName("모든 플레이어 찾기")
    void findAllPlayers() {
        playerRepository.save(new Player("nickname1", "A", "password"));
        playerRepository.save(new Player("nickname2", "B", "password"));
        playerRepository.save(new Player("nickname3", "C", "password"));
        final List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(3);
    }
}