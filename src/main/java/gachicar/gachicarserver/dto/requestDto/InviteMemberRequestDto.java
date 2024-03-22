package gachicar.gachicarserver.dto.requestDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InviteMemberRequestDto {
    private String nickname;
}
