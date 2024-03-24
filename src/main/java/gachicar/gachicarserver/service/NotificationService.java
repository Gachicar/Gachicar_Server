package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.NotificationType;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final static String NOTIFICATION_NAME = "notify";

    private final EmitterRepository emitterRepository;

    public SseEmitter connectNotification(String username) {
        // 새로운 SseEmitter를 만든다
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 유저 ID로 SseEmitter를 저장한다.
        emitterRepository.save(username, sseEmitter);

        // 세션이 종료될 경우 저장한 SseEmitter를 삭제한다.
        sseEmitter.onCompletion(() -> emitterRepository.delete(username));
        sseEmitter.onTimeout(() -> emitterRepository.delete(username));

        // 503 Service Unavailable 오류가 발생하지 않도록 첫 데이터를 보낸다.
        try {
            sseEmitter.send(SseEmitter.event().id("").name(NOTIFICATION_NAME).data("Connection completed"));
        } catch (IOException exception) {
            throw new ApiErrorException(ApiErrorStatus.NOTIFICATION_CONNECTION_ERROR);
        }
        return sseEmitter;
    }

    // 알림이 발생할 때마다 해당 메서드 호출
    public <T> void send(String nickname, T data, NotificationType type) {
        // 유저 nickname으로 SseEmitter를 찾아 이벤트를 발생 시킨다.
        emitterRepository.get(nickname).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(nickname)
                        .name(type.getMessage())
                        .data(data, MediaType.APPLICATION_JSON)
                        .comment(type.getComment()));
            } catch (IOException exception) {
                // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                emitterRepository.delete(nickname);
                throw new ApiErrorException(ApiErrorStatus.NOTIFICATION_CONNECTION_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

}
