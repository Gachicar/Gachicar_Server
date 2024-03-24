package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.ReportStatus;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.ReportDto;
import gachicar.gachicarserver.dto.UsageCountsDto;
import gachicar.gachicarserver.dto.UserDto;
import gachicar.gachicarserver.repository.DriveReportRepository;
import gachicar.gachicarserver.socket.CarSocketThread;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriveReportService {

    public final CarService carService;
    private final NotificationService notificationService;
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
    public void createReserveReportWithDest(User user, String destination) {

        Car userCar = carService.findByUser(user);

        DriveReport driveReport = new DriveReport(userCar, user, destination);
        reportRepository.save(driveReport);
    }

    /**
     * 예약 날짜 및 시간 설정
     */
    @Transactional
    public ReportDto setReserveDateAndTime(Long userId, String date, String hour, String minute) {

        // 날짜와 시간을 LocalDateTime으로 변환
        LocalDateTime dateTime = parseDateTime(date, hour, minute);

        DriveReport recentReport = getRecentReport(userId, ReportStatus.RESERVE);

        // 시작시간보다 늦게 끝나는 예약이 있는지 확인
        boolean exists = reportRepository.existsByStartTimeAndUserCar(dateTime, recentReport.getCar());
        if (!exists) {
            recentReport.setStartTime(dateTime);
            return new ReportDto(recentReport);
        } else {
            return null;
        }
    }

    @Transactional
    public ReportDto setReserveDriveTime(User user, String hour) {

        // hour 문자열에서 숫자를 추출하여 시간 값으로 파싱
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(hour);

        int hourValue = 0;
        if (matcher.find()) {
            hourValue = Integer.parseInt(matcher.group());
        }

        // 시간 값을 분으로 변환하여 driveTime에 할당
        Long driveTime = hourValue * 60L;

        DriveReport recentReport = getRecentReport(user.getId(), ReportStatus.RESERVE);

        LocalDateTime startTime = recentReport.getStartTime();

        // driveTime(분)을 더하여 endTime 설정
        LocalDateTime endTime = startTime.plusMinutes(driveTime);

        // 종료시간보다 일찍 시작하는 예약이 있는지 확인
        boolean exists = reportRepository.existsByEndTimeAndUserCar(endTime, recentReport.getCar());
        if (!exists) {
            recentReport.setEndTime(endTime);
            recentReport.setDriveTime(driveTime);

            return new ReportDto(recentReport);
        } else {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String date, String hour, String minute) {
        // 날짜를 LocalDateTime으로 파싱
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime parsedDate = LocalDateTime.parse(date, dateFormatter);

        // 시간을 LocalDateTime으로 파싱
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh시mm분");
        LocalDateTime parsedTime = LocalDateTime.parse(hour + minute, timeFormatter);

        // 날짜와 시간을 결합하여 하나의 LocalDateTime으로 반환
        return parsedDate.withHour(parsedTime.getHour()).withMinute(parsedTime.getMinute());
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

    /**
     * 예약된 시간에 사용자에게 알림 보내기
     */
    @Transactional
    @Scheduled(fixedRate = 60000) // 매 분마다 실행
    public void startRCtoReservation() {
        LocalDateTime now = LocalDateTime.now();
        // 예약된 시간 10분 전부터 현재 시간까지의 예약을 조회
        List<DriveReport> reportList = reportRepository.findByReservationTimeBetween(now.minusMinutes(30), now);

        for (DriveReport driveReport : reportList) {
            notificationService.sendRCStart(driveReport.getUser().getName(), new ReportDto(driveReport));

            // 차량 상태를 사용 중 상태로 변경
            Car car = driveReport.getCar();
            car.setCarStatus(Boolean.TRUE);
            Long userId = driveReport.getUser().getId();
            car.setNowUser(userId);

            CarSocketThread carSocketThread = new CarSocketThread(userId, this);
            carSocketThread.sendToCar(driveReport.getDestination());
            driveReport.setType(ReportStatus.RUNNING);
        }
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // 매 분마다 실행
    public void remindReservationTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMinutes(1);
        List<DriveReport> reportList = reportRepository.findByReservationTimeBetween(now, end);

        for (DriveReport driveReport : reportList) {
            notificationService.sendReserveReminder(driveReport.getUser().getName(), new ReportDto(driveReport));
        }
    }

}
