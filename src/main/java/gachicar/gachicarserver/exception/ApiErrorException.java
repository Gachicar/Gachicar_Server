package gachicar.gachicarserver.exception;

import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException{

    private final HttpStatusCode code;
    private final String errorMsg;

    public ApiErrorException(ApiErrorStatus errorStatus) {
        this.code = errorStatus.getCode();
        this.errorMsg = errorStatus.getMsg();
    }
}
