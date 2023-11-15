package gachicar.gachicarserver.exception;

import lombok.Getter;

@Getter
public class ApiErrorWithItemException extends RuntimeException{

    private final HttpStatusCode code;
    private final String errorMsg;

    public ApiErrorWithItemException(ApiErrorStatus errorStatus, Object item) {
        this.code = errorStatus.getCode();
        this.errorMsg = item + errorStatus.getMsg();
    }
}
