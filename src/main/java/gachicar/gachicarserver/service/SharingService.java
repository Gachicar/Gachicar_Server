package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CurLocationDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 카셰어링 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharingService {

    private final CarService carService;
    private final DriveReportService driveReportService;

    DriveReport driveReport = null;

    public CurLocationDto getCarLoc(Car car) {
        String loc = car.getCurLoc();
        if (loc == null) {
            throw new ApiErrorException(ApiErrorStatus.NOT_EXIST);
        } else {
            return new CurLocationDto(loc);
        }
    }


    @Transactional
    public void makeReport(User user, String destination, String command) throws IOException {

        Car car = carService.findByUser(user);

        // 공유차량의 현재 위치 확인
        String curLoc = car.getCurLoc();

        LocalDateTime startTime = LocalDateTime.now();

        try {
            // 주행기록 생성
            driveReport = new DriveReport(car, user, startTime, curLoc, destination);
            driveReportService.createReport(driveReport);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("주행 기록 생성에서 에러 발생");
        }

        // 주행 완료 후 현재 위치를 목적지로 설정
        car.setCurLoc(destination);

    }




}
