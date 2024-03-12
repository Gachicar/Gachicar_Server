package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.ReportDto;
import gachicar.gachicarserver.dto.UsageCountsDto;
import gachicar.gachicarserver.dto.UserDto;
import gachicar.gachicarserver.repository.DriveReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void updateReport(Long userId) {
        DriveReport driveReport = reportRepository.findRecentByUser(userId);

        LocalDateTime endTime = LocalDateTime.now();
        Duration diff = Duration.between(driveReport.getStartTime(), endTime);
        Long diffMin = diff.toMinutes();

        driveReport.setEndTime(endTime);
        driveReport.setDriveTime(diffMin);
    }

    public ReportDto getRecentReport(Long userId) {
        DriveReport driveReport = reportRepository.findRecentByUser(userId);
        if (driveReport != null) {
            return new ReportDto(driveReport);
        } else {
            return null;
        }
    }

    /**
     * 그룹 내 최다 사용자 조회
     */
    public UserDto getMostUserInGroupReport(Long carId) {
        User user = reportRepository.findUserWithMostUsageForCar(carId);
        return new UserDto(user);
    }

    /**
     * 그룹원별 공유차량 사용 횟수 조회
     */
    public List<UsageCountsDto> getUserUsageCounts(Long carId) {
        return reportRepository.getUserUsageCountsForCar(carId);
    }

}
