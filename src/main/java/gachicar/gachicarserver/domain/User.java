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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // 사용자 권한
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = Role.USER;
    }
}
