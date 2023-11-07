package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="Group_table")
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String name;
    private String desc;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<User> memberList = new ArrayList<>();

    private Long managerId; // 그룹장 아이디

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
