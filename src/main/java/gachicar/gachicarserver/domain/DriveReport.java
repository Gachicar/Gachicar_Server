package gachicar.gachicarserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Setter
    private Long driveTime;    // 주행시간

    private LocalDateTime startTime;   // 시작시간

    @Setter
    private LocalDateTime endTime;     // 종료시간

    private String departure;   // 출발지

    private String destination; // 목적지

    @Builder
    public DriveReport(Car car, User user, LocalDateTime startTime, String departure, String destination) {
        this.car = car;
        this.user = user;
        this.startTime = startTime;
        this.departure = departure;
        this.destination = destination;
    }
}
