package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {
    RUNNING("주행"),
    COMPLETE("완료"),
    RESERVE("예약");

    private final String type;
}
