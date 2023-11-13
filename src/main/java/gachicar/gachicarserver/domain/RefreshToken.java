package gachicar.gachicarserver.domain;

import lombok.Getter;

import javax.persistence.Id;

@Getter
public class RefreshToken {

    @Id
    private final String refreshToken;

    private final Long userId;

    public RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
