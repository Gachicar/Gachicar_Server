package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.repository.DriveReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriveReportService {

    public final CarService carService;
    public final DriveReportRepository reportRepository;

    @Transactional
    public void createReport(User user, String destination) {
        Car car = carService.findByUser(user);

        // 공유차량의 현재 위치 확인
        String curLoc = car.getCurLoc();

        LocalDateTime startTime = LocalDateTime.now();

        try {
            // 주행기록 생성
            DriveReport driveReport = new DriveReport(car, user, startTime, curLoc, destination);
            reportRepository.save(driveReport);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("주행 기록 생성에서 에러 발생");
        }
    }

}
