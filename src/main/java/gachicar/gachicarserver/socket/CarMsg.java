package gachicar.gachicarserver.socket;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CarMsg {
    END("종료"),
    HOME("집"),
    OFFICE("회사"),
    SCHOOL("학교"),
    START("시작"),
    STOP("정지"),
    COLLISION_AVOID("장애물 감지"),
    NORMAL("정상적으로 주행 중");

    private final String desc;
}
