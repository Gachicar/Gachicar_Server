package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.Car;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 공유차량 등록 시 차량 정보를 받아오는 DTO
 * 차량 정보 반환할 때에도 사용
 */
@Data
@NoArgsConstructor
public class CarDto {
    private String carName; // 등록할 차량 별명
    private String carNumber;   // 등록할 차량 번호
    private Long nowUser;   // 현재 사용자
    private String location;    // location: 자주 가는 목적지
    private Long driveTime;     // 시간
    private LocalDateTime latestDate;    // 날짜: 차를 최근에 사용한 날짜
    private String curLoc;      // 현재 위치

    public CarDto(Car car) {
        this.carName = car.getCarName();
        this.carNumber = car.getCarNumber();
        this.nowUser = car.getNowUser();
        this.location = car.getLocation();
        this.driveTime = car.getDriveTime();
        this.latestDate = car.getLatestDate();
        this.curLoc = car.getCurLoc();
    }
}
