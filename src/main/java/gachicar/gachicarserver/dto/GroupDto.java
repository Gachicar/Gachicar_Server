package gachicar.gachicarserver.dto;

import gachicar.gachicarserver.domain.GroupEntity;
import gachicar.gachicarserver.domain.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class GroupDto {
    private Long groupId;

    private String name;
    private String desc;
    private ManagerDto groupManager; // 그룹장
    private CarDto car;    // 공유차량
    private List<UserDto> memberList;

    public GroupDto(GroupEntity group) {
        this.groupId = group.getGroupId();
        this.name = group.getName();
        this.desc = group.getDesc();
        this.groupManager = new ManagerDto(group.getManager());

        try {
            this.car = new CarDto(group.getCar());
        } catch (NullPointerException e) {
            this.car = null;
        }

        this.memberList = group.getMemberList().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
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
