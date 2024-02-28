package gachicar.gachicarserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 현재 위치 가져오기
 */
@Data
@AllArgsConstructor
public class CurLocationDto {
    private String curLoc;
}
