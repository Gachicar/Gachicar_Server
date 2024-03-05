package gachicar.gachicarserver.api;

import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.exception.HttpStatusCode;
import gachicar.gachicarserver.service.SharingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Socket 통신 API Controller
 */
@RestController
@RequiredArgsConstructor
public class SocketApiController {
    public final SharingService sharingService;

    @GetMapping("/send")
    public ResultDto<Object> sendMessageToRC() {
        sharingService.socketClient("시작");

        return ResultDto.of(HttpStatusCode.OK, "ok", null);
    }
}
