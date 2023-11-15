package gachicar.gachicarserver.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String type;    // Signup / Login
    private String accessToken;
    private String refreshToken;

}
