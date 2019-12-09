package com.bb.stardium.player.domain;


import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.mediafile.MediaFile;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
public class Player2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private OffsetDateTime createDateTime;

    @UpdateTimestamp
    private OffsetDateTime updateDateTime;

    @Column(name = "nickname", length = 64, nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "statusMessage", length = 255)
    private String statusMessage = "";

    @ManyToMany(mappedBy = "players")
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private MediaFile profile;

    public void updateStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
