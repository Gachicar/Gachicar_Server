package gachicar.gachicarserver.api;

import gachicar.gachicarserver.config.jwt.CustomUserDetail;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.requestDto.AcceptInvitationRequestDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.dto.requestDto.InviteMemberRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.InviteService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteApiController {

    private final UserService userService;
    private final InviteService inviteService;

    /**
     * 초대할 멤버의 닉네임으로 초대
     */
    @PostMapping
    public ResultDto<Object> inviteMemberByNickname(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody InviteMemberRequestDto requestDto) {
        try {
            User user = userService.findUserById(userDetail.getId());
            inviteService.inviteMemberByNickname(user, user.getGroup(), requestDto);

            return ResultDto.of(HttpStatusCode.OK, "멤버 초대 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 그룹 초대 수락
     */
    @PostMapping("/accept")
    public ResultDto<Object> acceptInvitation(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody AcceptInvitationRequestDto requestDto) {
        try {
            User user = userService.findUserById(userDetail.getId());
            inviteService.acceptInvitation(user, requestDto);

            return ResultDto.of(HttpStatusCode.OK, "초대 수락 알림 전송 완료", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
