package gachicar.gachicarserver;

import gachicar.gachicarserver.domain.*;
import gachicar.gachicarserver.repository.CarRepository;
import gachicar.gachicarserver.repository.DriveReportRepository;
import gachicar.gachicarserver.repository.GroupRepository;
import gachicar.gachicarserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class InitDb {

    private final InitDbService initDbService;

    @PostConstruct
    public void init() {
        initDbService.initDb();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitDbService {
        private final UserRepository userRepository;
        private final GroupRepository groupRepository;
        private final CarRepository carRepository;
        private final DriveReportRepository driveReportRepository;

        public void initDb() {
            // 사용자 생성
            User user = User.builder()
                    .name("이연수")
                    .email("bestdustn@naver.com")
                    .build();
            user.setRole(Role.MANAGER);
            userRepository.save(user);

            // 그룹 생성
            GroupEntity newGroup = GroupEntity.builder()
                    .name("연수네그룹")
                    .desc("졸프 파이팅!")
                    .manager(user)
                    .build();
            groupRepository.save(newGroup);

            // 공유차량 생성
            Car newCar = Car.builder()
                    .name("가치카")
                    .number("12가9999")
                    .group(newGroup)
                    .build();
            carRepository.save(newCar);

            user.setGroup(newGroup);
            newGroup.setCar(newCar);

            // 다른 멤버 생성
            User user2 = User.builder()
                    .name("김예지")
                    .email("yeji@sm.ac.kr")
                    .build();
            userRepository.save(user2);

            User user3 = User.builder()
                    .name("임리안")
                    .email("rian@sm.ac.kr")
                    .build();
            userRepository.save(user3);

            User user4 = User.builder()
                    .name("아무개")
                    .email("someone@sm.ac.kr")
                    .build();
            userRepository.save(user4);

            user2.setGroup(newGroup);
            user3.setGroup(newGroup);

            // 그룹 멤버 리스트에 추가
            List<User> memberList = newGroup.getMemberList();
            memberList.add(user);
            memberList.add(user2);
            memberList.add(user3);
            newGroup.setMemberList(memberList);

            LocalDateTime dateTime1 = LocalDateTime.of(2024, 3, 15, 10, 30);
            LocalDateTime dateTime2 = LocalDateTime.of(2024, 3, 28, 15, 45);
            LocalDateTime dateTime3 = LocalDateTime.of(2024, 3, 5, 8, 0);
            LocalDateTime dateTime4 = LocalDateTime.of(2024, 3, 20, 12, 0);
            LocalDateTime dateTime5 = LocalDateTime.of(2024, 3, 10, 17, 20);
            LocalDateTime dateTime6 = LocalDateTime.of(2024, 3, 1, 9, 0);

            LocalDateTime dateTime11 = LocalDateTime.of(2024, 3, 15, 11, 0);
            LocalDateTime dateTime22 = LocalDateTime.of(2024, 3, 28, 16, 25);
            LocalDateTime dateTime33 = LocalDateTime.of(2024, 3, 5, 8, 40);
            LocalDateTime dateTime44 = LocalDateTime.of(2024, 3, 20, 12, 20);
            LocalDateTime dateTime55 = LocalDateTime.of(2024, 3, 10, 18, 10);
            LocalDateTime dateTime66 = LocalDateTime.of(2024, 3, 1, 9, 30);

            User[] userList = {user, user, user, user2, user2, user3};
            LocalDateTime[] dateTimes = {dateTime1, dateTime2, dateTime3, dateTime4, dateTime5, dateTime6,
                                            dateTime11, dateTime22, dateTime33, dateTime44, dateTime55, dateTime66};
            String[] departure = {"집", "집", "회사", "학교", "집", "학교"};
            String[] dests = {"회사", "회사", "집", "집", "회사", "집"};

            for (int i = 0; i < 6; i++) {
                Duration diff = Duration.between(dateTimes[i], dateTimes[i+6]);
                Long diffMin = diff.toMinutes();
                DriveReport driveReport = new DriveReport(newCar, userList[i], diffMin, dateTimes[i], dateTimes[i+6], departure[i], dests[i]);
                driveReportRepository.save(driveReport);
            }

            newCar.setLatestDate(LocalDateTime.of(2024, 3, 28, 16, 25));
            newCar.setDriveTime(210L);
            newCar.setDistance(497L);

            // 예약 리포트
            LocalDateTime dateTimeReserve = LocalDateTime.of(2024, 3, 25, 11, 20);      // 시작 시간
            LocalDateTime dateTimeReserve11 = LocalDateTime.of(2024, 3, 24, 12, 20);    // 종료 시간

            // user: 이연수, user2: 김예지, user3: 임리안
            DriveReport reserveReport = new DriveReport(newCar, user, "회사");
            reserveReport.setStartTime(dateTimeReserve);
            reserveReport.setEndTime(dateTimeReserve11);
            driveReportRepository.save(reserveReport);

            LocalDateTime dateTimeReserve2 = LocalDateTime.of(2024, 4, 1, 13, 0);       // 시작 시간
            LocalDateTime dateTimeReserve22 = LocalDateTime.of(2024, 4, 1, 15, 0);      // 종료 시간
            DriveReport reserveReport2 = new DriveReport(newCar, user2, "학교");
            reserveReport2.setStartTime(dateTimeReserve2);
            reserveReport2.setEndTime(dateTimeReserve22);
            driveReportRepository.save(reserveReport2);
        }
    }

}
