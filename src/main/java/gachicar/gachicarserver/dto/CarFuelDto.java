package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.Car;
import lombok.Data;

@Data
public class CarFuelDto {
    private String fuelType;
    private int oilStatus;

    public CarFuelDto(Car car) {
        this.fuelType = car.getFuelType();
        this.oilStatus = car.getOilStatus();
    }
}
