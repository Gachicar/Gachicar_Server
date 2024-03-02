//package gachicar.gachicarserver.socket;
//
//import gachicar.gachicarserver.config.jwt.JwtProvider;
//import gachicar.gachicarserver.chat.domain.Message;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    private final JwtProvider jwtProvider = null;
//    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    // 현재 연결된 세션들
//
//    // driveGroupCarId: {session1, session2}
//    // 공유차량 당 연결된 세션을 담은 Map
//    // Map<carId, Session Set>의 형태로 세션 저장
//    private final Map<Long, Set<WebSocketSession>> driveGroupCarSessionMap = new HashMap<>();
//
//    /**
//     * [소켓 연결 확인]
//     * 사용자가 웹 소켓 서버에 접속 시 동작하는 메소드
//     * - 이때 WebSocketSession 값이 생성됨. -> 이를 CLIENTS 변수에 담아준다.
//     */
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//        var sessionId = session.getId();
//        sessions.put(sessionId, session);   // 세션 저장
//
//        System.out.println("WebSocket Connection Established: " + session.getId());
//
//        Message message = Message.builder().sender(sessionId).receiver("all").build();
//        message.newConnect();
//
//        sessions.values().forEach(s -> {    // 모든 세션에 알림
//            try {
//                if(!s.getId().equals(sessionId)) {
//                    s.sendMessage(new TextMessage(Utils.getString(message)));
//                }
//            }
//            catch (Exception e) {
//                //TODO: throw
//            }
//        });
//    }
//
//    /**
//     * [소켓 종료 확인]
//     * 웹소켓 서버 접속이 끝났을 때 동작하는 메소드
//     * - 이때 CLIENTS 변수에 있는 해당 세션을 제거한다.
//     */
//    //소켓 연결 종료
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//
//        var sessionId = session.getId();
//
//        sessions.remove(sessionId);
//
//        final Message message = new Message();
//        message.closeConnect();
//        message.setSender(sessionId);
//
//        sessions.values().forEach(s -> {
//            try {
//                s.sendMessage(new TextMessage(Utils.getString(message)));
//            } catch (Exception e) {
//                //TODO: throw
//            }
//        });
//    }
//
//    /**
//     * [소켓 통신 시 메시지의 전송을 다루는 부분]
//     * 사용자의 메시지를 받으면 동작하는 메소드
//     */
//    //양방향 데이터 통신
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
//
//        String payload = textMessage.getPayload();
//        log.info("payload = {}", payload);
//
//        // token 검사
//        if (jwtProvider.validateToken(payload)) {
//            Authentication authentication = jwtProvider.getAuthentication(payload);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } else {
//            log.info("토큰이 유효하지 않습니다.");
//        }
//
//        System.out.println("Received message: " + textMessage.getPayload());
//        session.sendMessage(new TextMessage("Server received your message: " + textMessage.getPayload()));
//
//        /**
//         * [To-Do]
//         * - 공유차량과 메시지를 주고받는 DTO 생성
//         * - 공유차량의 상태를 나타내는 enum 생성
//         */
//
//        // payload -> carMessageDto로 변환
//
//        // 메모리 상에 공유차량에 대한 세션이 없으면 만들어줌.
//
//        // message 에 담긴 타입 확인
//        // 이때 message 에서 getType 으로 가져온 내용이
//        // CarDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면 sessions 에 넘어온 session 을 담는다.
//
//        Message message = Utils.getObject(payload);
//        message.setSender(session.getId());
//
//        WebSocketSession receiver = sessions.get(message.getReceiver());
//
//        if (receiver != null && receiver.isOpen()) {
//
//            receiver.sendMessage(new TextMessage(Utils.getString(message)));
//        }
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        //TODO:
//    }
//
//}
