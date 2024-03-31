package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.DriveReport;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주행 리포트 DTO
 */
@Data
@NoArgsConstructor
public class ReportDto {
    private String userName;
    private Long driveTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String departure;
    private String destination;

    public ReportDto(DriveReport report) {
        this.userName = report.getUser().getName();
        this.driveTime = report.getDriveTime();
        this.startTime = report.getStartTime();
        this.endTime = report.getEndTime();
        this.departure = report.getDeparture();
        this.destination = report.getDestination();
    }
}
