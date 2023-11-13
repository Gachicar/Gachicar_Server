package gachicar.gachicarserver.api;


import gachicar.gachicarserver.config.jwt.JwtProvider;
import gachicar.gachicarserver.config.jwt.dto.TokenDto;
import gachicar.gachicarserver.config.jwt.dto.TokenRefreshDto;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.AuthErrorStatus;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.external.client.kakao.dto.KakaoUserInfo;
import gachicar.gachicarserver.external.client.kakao.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 회원가입/로그인 API
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialLoginApiController {

    private final KakaoUserService kakaoUserService;
    private final JwtProvider jwtProvider;

    /**
     * 카카오 소셜 회원가입/로그인
     */
    @PostMapping("/api/auth/social")
    public ResultDto<Object> socialLogin(@RequestHeader HttpHeaders headers) {

        String accessToken = Objects.requireNonNull(headers.getFirst("Authorization")).substring(7);

        if (accessToken.equals("")) throw new AuthErrorException(AuthErrorStatus.EMPTY_TOKEN);

        try {
            // token으로 카카오 사용자 정보 가져오기
            KakaoUserInfo kakaoUserInfo = kakaoUserService.getKakaoUserInfo(accessToken);

            // 회원가입/로그인 후 JWT 토큰 발급
            TokenDto tokenDto = kakaoUserService.joinorLogin(kakaoUserInfo);

            if (tokenDto.getType().equals("Signup")) {
                return ResultDto.of(HttpStatusCode.CREATED, "회원 가입 성공", tokenDto);
            } else {
                return ResultDto.of(HttpStatusCode.CREATED, "로그인 성공", tokenDto);
            }
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 액세스 토큰 재발급 요청
     */
    @GetMapping("/api/auth/getnewtoken")
    public ResultDto<Object> getNewToken(@RequestHeader HttpHeaders headers) {
        try {
            String refreshToken = Objects.requireNonNull(headers.getFirst("Authorization")).substring(7);
            String accessToken = jwtProvider.reAccessToken(refreshToken);
            return ResultDto.of(HttpStatusCode.OK, "토큰 재발급", TokenRefreshDto.of(accessToken));
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
