package gachicar.gachicarserver.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="Group_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Setter
    private String name;
    @Setter
    private String desc;

    @Setter
    @OneToMany
    private List<User> memberList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager; // 그룹장

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @Builder
    public GroupEntity(String name, String desc, User manager) {
        this.name = name;
        this.desc = desc;
        this.manager = manager;
    }
}
