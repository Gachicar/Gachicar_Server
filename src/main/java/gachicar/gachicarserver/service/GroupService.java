package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.GroupDto;
import gachicar.gachicarserver.dto.requestDto.CreateGroupRequestDto;
import gachicar.gachicarserver.dto.requestDto.UpdateGroupNameRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    public final GroupRepository groupRepository;

    public Group createGroup(CreateGroupRequestDto requestDto, User user) {
        Group newGroup = Group.builder()
                .name(requestDto.getGroupName())
                .desc(requestDto.getGroupDesc())
                .manager(user)
                .build();
        groupRepository.save(newGroup);
        return newGroup;
    }

    public GroupDto getUserGroup(User user) {
        return new GroupDto(user.getGroup());
    }

    /* 그룹 닉네임 수정 */
    @Transactional
    public void updateGroupName(User user, UpdateGroupNameRequestDto requestDto) {
        Group group = user.getGroup();

        // 사용자가 그룹장인지 확인
        if (group.getManager() == user) {
            group.setName(requestDto.getNewGroupName());
        } else {
            throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
        }
    }
}
