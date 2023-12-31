package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

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

    @Setter
    private Long distance;  // 주행거리

    @Setter
    private String oilStatus;  // 주유 상태

    private String fuelType;    // fuelType

    private int rating;     // rating

    @Setter
    private String location;    // location: 자주 가는 목적지

    @Setter
    private Long driveTime;     // 시간

    @Setter
    private Date latestDate;    // 날짜: 차를 최근에 사용한 날짜


}
