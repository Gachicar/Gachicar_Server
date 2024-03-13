package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CarDto;
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

        Group group = user.getGroup();

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

}
