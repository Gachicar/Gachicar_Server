package gachicar.gachicarserver.dto.requestDto;

import lombok.Data;

/**
 * 그룹 생성 시 입력하는 데이터를 받아오는 DTO
 */
@Data
public class CreateGroupRequestDto {
    private String groupName;
    private String groupDesc;
}
