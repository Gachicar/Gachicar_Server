package gachicar.gachicarserver.config.jwt.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRefreshDto {
    private String accessToken;

    public static TokenRefreshDto of(String accessToken) {
        return new TokenRefreshDto(accessToken);
    }
}
