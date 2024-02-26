package gachicar.gachicarserver.dto.requestDto;

import lombok.Data;

/**
 * 주행 시작 명령 시 client 요청을 받아오는 DTO
 * - 현재 위치
 * - 목적지
 */
@Data
public class StartDriveRequestDto {
    private String curLoc;
    private String destination;
}
