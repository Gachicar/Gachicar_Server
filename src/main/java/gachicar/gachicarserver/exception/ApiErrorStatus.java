package gachicar.gachicarserver.exception;

import lombok.Getter;

/**
 * API Error Code
 */
@Getter
public enum ApiErrorStatus {

    /**
     * 사용자 Api 관련 에러 코드
     */
    DUPLICATED_USER_NAME(HttpStatusCode.BAD_REQUEST, "중복된 닉네임입니다."),
    NOT_EXIST_NAME(HttpStatusCode.BAD_REQUEST, "존재하지 않는 닉네임입니다."),

    /**
     * 그룹 Api 관련 에러 코드
     */
    DUPLICATED_GROUP_NAME(HttpStatusCode.BAD_REQUEST, "이미 존재하는 그룹 이름입니다."),
    NOT_MANAGER(HttpStatusCode.UNAUTHORIZED, "그룹장만 수정할 수 있습니다."),
    NOT_HAVE_GROUP(HttpStatusCode.BAD_REQUEST, "사용자가 속한 그룹이 없습니다."),
    MALFORMED(HttpStatusCode.BAD_REQUEST, "형식이 잘못되었습니다."),
    ALREADY_MEMBER(HttpStatusCode.BAD_REQUEST, "사용자가 이미 속한 그룹입니다."),

    /**
     * 공유차량 Api 관련 에러 코드
     */
    NOT_EXIST(HttpStatusCode.BAD_REQUEST, " 존재하지 않습니다."),

    /**
     * Socket 통신 관련 에러 코드
     */
    SOCKET_ERROR(HttpStatusCode.INTERNAL_SERVER_ERROR, "소켓 통신 중 문제 발생"),
    CONNECT_FAILED(HttpStatusCode.INTERNAL_SERVER_ERROR, "소켓 연결 실패"),
    DISCONNECT_FAILED(HttpStatusCode.INTERNAL_SERVER_ERROR, "연결 해제 실패"),
    PORT_ERROR(HttpStatusCode.INTERNAL_SERVER_ERROR, "포트 접속 실패"),

    /**
     * Report 관련 에러 코드
     */
    NO_RESULT(HttpStatusCode.NO_CONTENT, "내용 없음"),

    /**
     * 알림 관련 에러 코드
     */
    NOTIFICATION_CONNECTION_ERROR(HttpStatusCode.INTERNAL_SERVER_ERROR, "알림 서비스 연결 실패");

    private final HttpStatusCode code;
    private final String msg;

    ApiErrorStatus(HttpStatusCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
