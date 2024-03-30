package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.*;
import gachicar.gachicarserver.dto.CarDto;
import gachicar.gachicarserver.dto.requestDto.UpdateCarNameRequestDto;
import gachicar.gachicarserver.dto.requestDto.UpdateCarNumberRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.exception.ApiErrorWithItemException;
import gachicar.gachicarserver.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공유차량 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    public final CarRepository carRepository;
    public final GroupService groupService;

    /**
     * 공유차량 등록
     */
    @Transactional
    public Car createCar(CarDto carDto, User user) {

        GroupEntity group = user.getGroup();

        String carNum = carDto.getCarNumber();
        Car car = carRepository.findByNumber(carNum);

        // 사용자가 그룹장인지 확인
        if (!group.getManager().equals(user)) {
            throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);

        } else if (carDto.getCarName().equals("")) {
            throw new ApiErrorWithItemException(ApiErrorStatus.NOT_EXIST, "carName");

        } else if (carDto.getCarNumber().equals("")) {
            throw new ApiErrorWithItemException(ApiErrorStatus.NOT_EXIST, "carNumber");
        }

        if (car == null) {
            Car newCar = Car.builder()
                    .name(carDto.getCarName())
                    .number(carNum)
                    .group(group)
                    .build();
            carRepository.save(newCar);
            // 그룹에 차량 설정
            groupService.updateGroupCar(group, newCar);
        }

        return car;
    }

    public Car findByUser(User user) {
        return carRepository.findByGroupId(user.getGroup().getGroupId());
    }

    // 주행 완료 후 차량 정보 업데이트
    public void updateCarStatus(DriveReport driveReport, String favoriteDest) {
        Car car = driveReport.getCar();
        car.setCarStatus(Boolean.FALSE);
        car.setCurLoc(driveReport.getDestination());
        car.setDriveTime(car.getDriveTime() + driveReport.getDriveTime());
        car.setNowUser(null);
        car.setLatestDate(driveReport.getEndTime());
        car.setOilStatus(car.getOilStatus()-5);
        car.setDistance(car.getDistance() + car.getDriveTime()*50);

        car.setLocation(favoriteDest);
    }

    /* 공유차량 이름 수정 */
    public void updateCarName(User user, UpdateCarNameRequestDto requestDto) {
        Car car = user.getGroup().getCar();

        if (car == null) {
            throw new ApiErrorWithItemException(ApiErrorStatus.NOT_EXIST, "공유차량이");
        } else {
            // 사용자가 그룹장인지 확인
            if (user.getRole() == Role.MANAGER) {
                // 차량 이름 수정
                car.setCarName(requestDto.getCarName());
            } else {
                throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
            }
        }
    }

    /* 공유차량 번호 수정 */
    public void updateCarNumber(User user, UpdateCarNumberRequestDto requestDto) {
        Car car = user.getGroup().getCar();

        if (car == null) {
            throw new ApiErrorWithItemException(ApiErrorStatus.NOT_EXIST, "공유차량이");
        } else {
            // 사용자가 그룹장인지 확인
            if (user.getRole() == Role.MANAGER) {
                // 차량 번호 수정
                car.setCarNumber(requestDto.getCarNumber());
            } else {
                throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
            }
        }
    }

}
