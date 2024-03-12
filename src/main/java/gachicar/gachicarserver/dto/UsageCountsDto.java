package gachicar.gachicarserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsageCountsDto {
    private UserDto user;
    private Long count;
}
