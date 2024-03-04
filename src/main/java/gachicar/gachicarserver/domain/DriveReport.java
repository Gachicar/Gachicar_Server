package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "DriveReport")
@NoArgsConstructor
public class DriveReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;            // 공유차량

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;          // 사용자

    private float driveTime;    // 주행시간

    private String startTime;   // 시작시간
    private String endTime;     // 종료시간

    private String departure;   // 출발지

    private String destination; // 목적지
}
