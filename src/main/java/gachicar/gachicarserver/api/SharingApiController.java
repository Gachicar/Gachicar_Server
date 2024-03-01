package gachicar.gachicarserver.api;

import gachicar.gachicarserver.config.jwt.CustomUserDetail;
import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CurLocationDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.dto.requestDto.StartDriveRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.SharingService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public String getCarStatus(@AuthenticationPrincipal CustomUserDetail userDetail) {
        return "정상 운행 중";
    }

    /**
     * 차량의 현재 위치 가져오기
     */
    @GetMapping("/loc")
    public ResultDto<Object> getCurLocation(@AuthenticationPrincipal CustomUserDetail userDetail) {
        try {
            User user = userService.findUserById(userDetail.getId());

            // 사용자의 공유차량 가져오기
            Car car = user.getGroup().getCar();

            // 공유차량의 현재 위치 확인
            CurLocationDto carLoc = sharingService.getCarLoc(car);

            return ResultDto.of(HttpStatusCode.OK, "현재 위치 가져오기 성공", carLoc);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
//        catch (Exception e) {
//            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
//        }
    }

    /**
     * 목적지로 주행 시작
     */
    @PostMapping("/go")
    public ResultDto<Object> startDrive(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody StartDriveRequestDto requestDto) {
        try {
            User user = userService.findUserById(userDetail.getId());

            // 사용자의 공유차량 가져오기
            Car car = user.getGroup().getCar();

            // 사용 가능 상태인지 확인
            if (car.getCarStatus()) {
                return ResultDto.of(HttpStatusCode.BAD_REQUEST, "다른 사용자가 사용 중", car.getNowUser());
            }

            sharingService.startDrive(car, requestDto);

            return ResultDto.of(HttpStatusCode.OK, "주행 시작", null);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiErrorException(ApiErrorStatus.SOCKET_ERROR);
        }
//        catch (Exception e) {
//            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
//        }

        // 목적지로 주행
        // 주기적으로 상태 보냄.
    }
}
