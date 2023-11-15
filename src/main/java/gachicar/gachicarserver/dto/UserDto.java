package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.User;
import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String userName;

    public UserDto(User user) {
        userId = user.getId();
        userName = user.getName();
    }
}
