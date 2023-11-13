package gachicar.gachicarserver.external.client.kakao.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class KakaoUserInfo {
    private Long id;
    private String name;
    private Map<String, Object> kakao_account;
    private Map<String, Object> profile;
    private String email;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.id = (Long) attributes.get("id");
        this.kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) kakao_account.get("profile");
        this.name = (String) profile.get("nickname");
        this.email = (String) kakao_account.get("email");
    }
}
