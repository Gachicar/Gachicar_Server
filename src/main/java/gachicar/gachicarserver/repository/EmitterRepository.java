package gachicar.gachicarserver.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class EmitterRepository {

    // 유저ID를 키로 SseEmitter를 해시맵에 저장할 수 있도록 구현했다.
    private Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter save(String nickname, SseEmitter sseEmitter) {
        emitterMap.put(nickname, sseEmitter);
        log.info("Saved SseEmitter for {}", nickname);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(String nickname) {
        log.info("Got SseEmitter for {}", nickname);
        return Optional.ofNullable(emitterMap.get(nickname));
    }

    public void delete(String nickname) {
        emitterMap.remove(nickname);
        log.info("Deleted SseEmitter for {}", nickname);
    }

}
