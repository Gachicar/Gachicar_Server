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
    private Map<String, Object> properties;
    private String profile_image;
    private String email;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.id = (Long) attributes.get("id");
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.name = (String) properties.get("nickname");
        this.profile_image = (String) properties.get("profile_image");
        this.kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        this.email = (String) kakao_account.get("email");
    }
}
