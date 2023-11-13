package gachicar;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // driveGroupCarId: {session1, session2}
    // 공유차량 당 연결된 세션을 담은 Map
    // Map<carId, Session Set>의 형태로 세션 저장
    private final Map<Long, Set<WebSocketSession>> driveGroupCarSessionMap = new HashMap<>();

    /**
     * [소켓 연결 확인]
     * 사용자가 웹 소켓 서버에 접속 시 동작하는 메소드
     * - 이때 WebSocketSession 값이 생성됨. -> 이를 CLIENTS 변수에 담아준다.
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    /**
     * [소켓 종료 확인]
     * 웹소켓 서버 접속이 끝났을 때 동작하는 메소드
     * - 이때 CLIENTS 변수에 있는 해당 세션을 제거한다.
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    /**
     * [소켓 통신 시 메시지의 전송을 다루는 부분]
     * 사용자의 메시지를 받으면 동작하는 메소드
     */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        /**
         * [To-Do]
         * - 공유차량과 메시지를 주고받는 DTO 생성
         * - 공유차량의 상태를 나타내는 enum 생성
         */

        // payload -> carMessageDto로 변환

        // 메모리 상에 공유차량에 대한 세션이 없으면 만들어줌.

        // message 에 담긴 타입 확인
        // 이때 message 에서 getType 으로 가져온 내용이
        // CarDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면 sessions 에 넘어온 session 을 담는다.

    }

}
