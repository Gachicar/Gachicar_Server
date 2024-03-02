package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GroupDto {
    private Long groupId;

    private String name;
    private String desc;
    private ManagerDto groupManager; // 그룹장
    private Car car;    // 공유차량

    public GroupDto(Group group) {
        this.groupId = group.getGroupId();
        this.name = group.getName();
        this.desc = group.getDesc();
        this.groupManager = new ManagerDto(group.getManager());
    }
}

@Data
class ManagerDto {
    private Long id;
    private String name;

    public ManagerDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
