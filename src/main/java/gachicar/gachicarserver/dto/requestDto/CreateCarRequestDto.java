package gachicar.gachicarserver.dto.requestDto;

import lombok.Data;

/**
 * 공유차량 등록 시 차량 정보를 받아오는 DTO
 */
@Data
public class CreateCarRequestDto {
    private String carName; // 등록할 차량 별명
    private String carNumber;   // 등록할 차량 번호
}
