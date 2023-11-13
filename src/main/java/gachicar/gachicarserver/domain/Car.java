package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Car")
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carName;

    @OneToOne(mappedBy = "car", cascade = CascadeType.REMOVE)
    private Group group;   // 공유차량이 속한 그룹

    @Setter
    private Boolean carStatus;  // 사용중 TRUE / 사용 가능 FALSE

    @Setter
    private Long nowUser;   // 현재 사용자

    public Car(String carName) {
        this.carName = carName;
    }
}
