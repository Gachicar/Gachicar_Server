package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.Car;
import lombok.Data;

import java.util.List;

@Data
public class ReportListDto {
    private CarDto car;
    private List<ReportDto> report;

    public ReportListDto(Car car, List<ReportDto> report) {
        this.car = new CarDto(car);
        this.report = report;
    }
}
