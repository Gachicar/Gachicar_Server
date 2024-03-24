package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    INVITE_MEMBER("invite","회원님을 그룹에 초대하고 싶어 합니다."),
    ACCEPT_INVITATION("accept", "초대를 수락했습니다."),
    START_RC("car", "예약시간 30분 전이 되어 차량이 출발합니다."),
    REMINDER("reminder", "예약시간이 되었습니다.");

    private final String message;
    private final String comment;
}
