package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.dto.CurLocationDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 카셰어링 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharingService {

    private final CarService carService;

    public CurLocationDto getCarLoc(Car car) {
        String loc = car.getCurLoc();
        if (loc == null) {
            throw new ApiErrorException(ApiErrorStatus.NOT_EXIST);
        } else {
            return new CurLocationDto(loc);
        }
    }




}
