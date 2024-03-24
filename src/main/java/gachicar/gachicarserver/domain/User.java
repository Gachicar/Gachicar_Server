package gachicar.gachicarserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    private String email;

    // 내가 속한 그룹
    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    // 사용자 권한
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.role = Role.USER;
    }
}
