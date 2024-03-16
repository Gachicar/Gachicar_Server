package gachicar.gachicarserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Car")
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carName;
    private String carNumber;

    @Setter
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;   // 공유차량이 속한 그룹

    @Setter
    private Boolean carStatus = Boolean.FALSE;  // 사용중 TRUE / 사용 가능 FALSE

    @Setter
    private Long nowUser;   // 현재 사용자

    @Setter
    private Long distance;  // 주행거리

    @Setter
    private int oilStatus = 100;  // 주유 상태: 0~100

    private String fuelType = "휘발유";    // fuelType

    private int rating;     // rating

    @Setter
    private String location = "학교";    // location: 자주 가는 목적지

    @Setter
    private Long driveTime;     // 시간

    @Setter
    private LocalDateTime latestDate;    // 날짜: 차를 최근에 사용한 날짜

    @Setter
    private String curLoc = "집";      // 현재 위치

    @Builder
    public Car(String name, String number, Group group) {
        this.carName = name;
        this.carNumber = number;
        this.group = group;
    }
}
