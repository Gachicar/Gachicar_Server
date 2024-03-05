package gachicar.gachicarserver;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.Role;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.repository.CarRepository;
import gachicar.gachicarserver.repository.GroupRepository;
import gachicar.gachicarserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

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

        public void initDb() {
            // 사용자 생성
            User user = User.builder()
                    .name("이연수")
                    .email("bestdustn@sookmyung.ac.kr")
                    .build();
            user.setRole(Role.MANAGER);
            userRepository.save(user);

            // 그룹 생성
            Group newGroup = Group.builder()
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
        }
    }

}
