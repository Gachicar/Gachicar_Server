package gachicar.gachicarserver.api;

import gachicar.gachicarserver.config.jwt.CustomUserDetail;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.CurLocationDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.dto.requestDto.StartDriveRequestDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarCommandApiController {

    public final UserService userService;

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
            // 공유차량의 현재 위치 확인
            CurLocationDto curLoc = new CurLocationDto();   // 현재 위치 DTO 에 담기

            return ResultDto.of(HttpStatusCode.OK, "현재 위치 가져오기 성공", curLoc);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
//        catch (Exception e) {
//            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
//        }
        // 현재 위치는 가장 마지막 목적지로 가정
    }

    /**
     * 목적지로 주행 시작
     */
    @PostMapping("/go")
    public ResultDto<Object> startDrive(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody StartDriveRequestDto requestDto) {
        try {
            User user = userService.findUserById(userDetail.getId());

            // 사용자의 공유차량 가져오기
            // 공유차량이 사용 가능 상태인지 확인
            // 공유차량의 현재 위치 확인
            // 목적지로 주행 시작

            return ResultDto.of(HttpStatusCode.OK, "주행 시작", null);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        }
//        catch (Exception e) {
//            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
//        }

        // 목적지로 주행
        // 주기적으로 상태 보냄.
    }
}
