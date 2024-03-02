package gachicar.gachicarserver.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 사용자"),
    MANAGER("ROLE_GROUP_MANAGER", "그룹 관리자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
