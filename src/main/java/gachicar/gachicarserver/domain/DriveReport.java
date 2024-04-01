package gachicar.gachicarserver.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "DriveReport")
@NoArgsConstructor
@AllArgsConstructor
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

    @Setter
    private LocalDateTime startTime;   // 시작시간

    @Setter
    private LocalDateTime endTime;     // 종료시간

    private String departure;   // 출발지

    @Setter
    private String destination; // 목적지

    @Setter
    private ReportStatus type;    // 주행, 완료, 예약

    private LocalDateTime created_at;   // 리포트 생성 시간

    // 주행 리포트
    @Builder
    public DriveReport(Car car, User user, LocalDateTime startTime, String departure, String destination) {
        this.car = car;
        this.user = user;
        this.startTime = startTime;
        this.departure = departure;
        this.destination = destination;
        this.type = ReportStatus.RUNNING;
        this.created_at = LocalDateTime.now();
    }

    public DriveReport(Car car, User user, Long driveTime, LocalDateTime startTime, LocalDateTime endTime, String departure, String destination) {
        this.car = car;
        this.user = user;
        this.driveTime = driveTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.departure = departure;
        this.destination = destination;
        this.type = ReportStatus.COMPLETE;
    }

    // 예약 리포트
    public DriveReport(Car car, User user, String destination) {
        this.car = car;
        this.user = user;
        this.destination = destination;
        this.type = ReportStatus.RESERVE;
        this.created_at = LocalDateTime.now();
    }
}
