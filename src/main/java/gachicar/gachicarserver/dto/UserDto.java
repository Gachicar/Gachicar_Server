package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class UserDto {
    private Long userId;
    private String userName;

    public UserDto(User user) {
        userId = user.getId();
        userName = user.getName();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;

        public static Response of(User user) {
            return Response.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
        }
    }
}
