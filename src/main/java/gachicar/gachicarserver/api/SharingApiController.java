package gachicar.gachicarserver.api;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CurLocationDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.SharingService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 공유차량 주행 관련 API Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sharing")
public class SharingApiController {

    public final UserService userService;
    public final CarService carService;
    public final SharingService sharingService;

    @GetMapping
    public String getCarStatus() {
        return "정상 운행 중";
    }

    /**
     * 차량의 현재 위치 가져오기
     */
    @GetMapping("/loc")
    public ResultDto<Object> getCurLocation() {
        try {
            User user = userService.findUserById(1L);
            // 사용자의 공유차량 가져오기
            Car car = user.getGroup().getCar();

            // 공유차량의 현재 위치 확인
            CurLocationDto carLoc = sharingService.getCarLoc(car);

            return ResultDto.of(HttpStatusCode.OK, "현재 위치 가져오기 성공", carLoc);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
        catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

}
