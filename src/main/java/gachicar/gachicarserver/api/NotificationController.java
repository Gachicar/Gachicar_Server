package gachicar.gachicarserver.api;

import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.service.NotificationService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long userId) {
        // 사용자 정보 받아오기
//        Long userId = 1L;
        User user = userService.findUserById(userId);

        // 서비스를 통해 생성된 SseEmitter를 반환
        return notificationService.connectNotification(user.getName());
    }
}
