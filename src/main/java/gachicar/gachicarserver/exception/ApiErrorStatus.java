package gachicar.gachicarserver.exception;

import lombok.Getter;

/**
 * API Error Code
 */
@Getter
public enum ApiErrorStatus {

    /**
     * 그룹 Api 관련 에러 코드
     */
    NOT_MANAGER(HttpStatusCode.UNAUTHORIZED, "그룹장만 수정할 수 있습니다."),
    NOT_HAVE_GROUP(HttpStatusCode.BAD_REQUEST, "사용자가 속한 그룹이 없습니다."),

    /**
     * 공유차량 Api 관련 에러 코드
     */
    DUPLICATED_NUMBER(HttpStatusCode.BAD_REQUEST, " 차량번호가 중복되었습니다."),
    NOT_EXIST(HttpStatusCode.BAD_REQUEST, "가 존재하지 않습니다.");

    private final HttpStatusCode code;
    private final String msg;

    ApiErrorStatus(HttpStatusCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
