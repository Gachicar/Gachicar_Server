package gachicar.gachicarserver.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="Group_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String name;
    private String desc;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<User> memberList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager; // 그룹장

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Builder
    public Group(String name, String desc, User manager) {
        this.name = name;
        this.desc = desc;
        this.manager = manager;
    }
}
