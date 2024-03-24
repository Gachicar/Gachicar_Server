package gachicar.gachicarserver.api;


import gachicar.gachicarserver.config.jwt.CustomUserDetail;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.dto.UserDto;
import gachicar.gachicarserver.dto.requestDto.UpdateUserNicknameRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @GetMapping("/api/user")
    public ResultDto<UserDto> getUserInfo(@AuthenticationPrincipal CustomUserDetail userDetail) {
        try {
            User user = userService.findUserById(userDetail.getId());
            return ResultDto.of(HttpStatusCode.OK, "유저 조회 성공", new UserDto(user));

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @PatchMapping("/api/user")
    public ResultDto<Object> updateUserNickname(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        try {
            userService.update(userDetail.getId(), updateUserNicknameRequestDto);
            return ResultDto.of(HttpStatusCode.CREATED, "사용자 닉네임 설정 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @DeleteMapping("/api/user")
    public ResultDto<Object> deleteUser(@AuthenticationPrincipal CustomUserDetail userDetail) {
        userService.delete(userDetail.getId());
        return ResultDto.of(HttpStatusCode.OK, "사용자 탈퇴 성공", null);
    }
}
