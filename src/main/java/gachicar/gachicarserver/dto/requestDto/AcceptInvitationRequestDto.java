package gachicar.gachicarserver.dto.requestDto;

import lombok.Data;

/**
 * 그룹 초대 수락할 때 사용
 */
@Data
public class AcceptInvitationRequestDto {
    private Long groupId;
}
