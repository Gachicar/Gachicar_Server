package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.DriveReport;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarReportDto {
    private CarDto car;
    private String userName;
    private Long driveTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String departure;
    private String destination;

    public CarReportDto(DriveReport report) {
        this.car = new CarDto(report.getCar());
        this.userName = report.getUser().getName();
        this.driveTime = report.getDriveTime();
        this.startTime = report.getStartTime();
        this.endTime = report.getEndTime();
        this.departure = report.getDeparture();
        this.destination = report.getDestination();
    }
}
