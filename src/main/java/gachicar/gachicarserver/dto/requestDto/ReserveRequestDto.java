package gachicar.gachicarserver.dto.requestDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReserveRequestDto {
    private String intention;
    private String response;
    private String destination;
    private String date;
    private String hour;
    private String minute;
}
