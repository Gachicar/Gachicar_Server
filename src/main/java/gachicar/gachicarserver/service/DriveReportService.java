package gachicar.gachicarserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.ReportStatus;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.ReportDto;
import gachicar.gachicarserver.dto.UsageCountsDto;
import gachicar.gachicarserver.dto.UserDto;
import gachicar.gachicarserver.repository.DriveReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        DriveReport driveReport = reportRepository.findRecentByUser(userId, ReportStatus.RUNNING);

        LocalDateTime endTime = LocalDateTime.now();
        Duration diff = Duration.between(driveReport.getStartTime(), endTime);
        Long diffMin = diff.toMinutes();

        driveReport.setEndTime(endTime);
        driveReport.setDriveTime(diffMin);

        driveReport.setType(ReportStatus.COMPLETE);

        carService.updateCarStatus(driveReport, reportRepository.getFavoriteDestination(driveReport.getCar().getId()));
    }

    /**
     * 특정 사용자의 최근 주행 리포트 or 최근 예약 내역 조회
     */
    public DriveReport getRecentReport(Long userId, ReportStatus type) {
        return reportRepository.findRecentByUser(userId, type);
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

    /**
     * 사용자의 모든 주행 기록 or 예약 내역 조회
     */
    public List<ReportDto> getAllReportsByUser(Long userId, ReportStatus type) {
        List<DriveReport> driveReports = reportRepository.findAllByUser(userId, type);
        return driveReports.stream()
                .map(ReportDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 그룹의 모든 주행 기록 or 예약 내역 조회
     */
    public List<ReportDto> getAllReportsByGroup(Long carId, ReportStatus type) {
        List<DriveReport> driveReports = reportRepository.findAllByGroup(carId, type);
        return driveReports.stream()
                .map(ReportDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 예약 리포트 생성
     */
    @Transactional
    public ReportDto createReserveReport(User user, String destination, String timeStr) throws JsonProcessingException {
        int hour = 0;

        // 정규 표현식을 사용하여 숫자 부분을 추출
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(timeStr);

        if (matcher.find()) {
            // 숫자 부분을 정수로 변환
            hour = Integer.parseInt(matcher.group());
        }

        Car userCar = carService.findByUser(user);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(hour, 0);
        LocalDateTime endTime = LocalDateTime.of(date, time);

        // 중복 확인
        boolean isReservationExist = reportRepository.existsByEndTimeAndUserCar(endTime, userCar);
        if (isReservationExist) {
            // 이미 같은 시간에 예약이 있음
            return null;
        }

        DriveReport driveReport = new DriveReport(userCar, user, endTime, destination);
        reportRepository.save(driveReport);

        return new ReportDto(driveReport);
    }


    @Transactional
    public void completeDriveReport(Long userId, String dest) {
        DriveReport recentReport = getRecentReport(userId, ReportStatus.RUNNING);
        recentReport.setDestination(dest);

        LocalDateTime endTime = LocalDateTime.now();
        recentReport.setEndTime(endTime);

        Duration diff = Duration.between(recentReport.getStartTime(), endTime);
        Long diffMin = diff.toMinutes();

        recentReport.setDriveTime(recentReport.getDriveTime()+diffMin);
        recentReport.setType(ReportStatus.COMPLETE);
    }


}
