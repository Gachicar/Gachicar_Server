package gachicar.gachicarserver.api;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CarDto;
import gachicar.gachicarserver.dto.CarFuelDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.GroupService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarApiController {

    public final UserService userService;
    public final GroupService groupService;
    public final CarService carService;

    @PostMapping
    public ResultDto<Object> createCar(@RequestBody CarDto carRequestDto) {
        try {
            User user = userService.findUserById(1L);
//            User user = userService.findUserById(userDetail.getId());

            // 사용자의 공유차량 등록
            Car car = carService.createCar(carRequestDto, user);

            return ResultDto.of(HttpStatusCode.OK, "공유차량 등록 성공", car.getId());

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
        catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @GetMapping
    public ResultDto<Object> getCarInfo() {
        try {
            User user = userService.findUserById(1L);
            Car car = user.getGroup().getCar();

            if (car == null) {
                return ResultDto.of(HttpStatusCode.BAD_REQUEST, "공유차량이 존재하지 않습니다.", null);
            }

            return ResultDto.of(HttpStatusCode.OK, "공유차량 정보 조회 성공", new CarDto(car));
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
        catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @GetMapping("/fuel")
    public ResultDto<Object> getCarFuel() {
        try {
            User user = userService.findUserById(1L);
            Car car = user.getGroup().getCar();

            if (car == null) {
                return ResultDto.of(HttpStatusCode.BAD_REQUEST, "공유차량이 존재하지 않습니다.", null);
            }
            return ResultDto.of(HttpStatusCode.OK, "공유차량 연료 조회 성공", new CarFuelDto(car));
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
//        catch (Exception e) {
//            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
//        }
    }
}
