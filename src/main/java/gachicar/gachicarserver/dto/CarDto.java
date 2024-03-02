package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.Car;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공유차량 등록 시 차량 정보를 받아오는 DTO
 * 차량 정보 반환할 때에도 사용
 */
@Data
@NoArgsConstructor
public class CarDto {
    private String carName; // 등록할 차량 별명
    private String carNumber;   // 등록할 차량 번호

    public CarDto(Car car) {
        this.carName = car.getCarName();
        this.carNumber = car.getCarNumber();
    }
}
