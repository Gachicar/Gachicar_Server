package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.GroupEntity;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.GroupDto;
import gachicar.gachicarserver.dto.requestDto.CreateGroupRequestDto;
import gachicar.gachicarserver.dto.requestDto.DeleteGroupRequestDto;
import gachicar.gachicarserver.dto.requestDto.UpdateGroupDescRequestDto;
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

    public GroupEntity createGroup(CreateGroupRequestDto requestDto, User user) {
        if (groupRepository.findByName(requestDto.getGroupName()) == null) {
            GroupEntity newGroup = GroupEntity.builder()
                    .name(requestDto.getGroupName())
                    .desc(requestDto.getGroupDesc())
                    .manager(user)
                    .build();
            groupRepository.save(newGroup);
            return newGroup;
        } else {
            throw new ApiErrorException(ApiErrorStatus.DUPLICATED_GROUP_NAME);
        }
    }

    public GroupDto getUserGroup(User user) {
        return new GroupDto(user.getGroup());
    }

    /* 그룹 닉네임 수정 */
    @Transactional
    public void updateGroupName(User user, UpdateGroupNameRequestDto requestDto) {
        GroupEntity group = user.getGroup();

        // 사용자가 그룹장인지 확인
        if (group.getManager() == user) {
            group.setName(requestDto.getNewGroupName());
        } else {
            throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
        }
    }

    /* 그룹 한줄소개 수정 */
    @Transactional
    public void updateGroupDesc(User user, UpdateGroupDescRequestDto requestDto) {
        GroupEntity group = user.getGroup();

        // 사용자가 그룹장인지 확인
        if (group.getManager() == user) {
            group.setName(requestDto.getNewDesc());
        } else {
            throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
        }
    }

    /* 그룹 자체를 삭제 (그룹장만 가능) */
    @Transactional
    public void deleteGroup(DeleteGroupRequestDto requestDto) {
        GroupEntity group = groupRepository.findById(requestDto.getDeleteId());
        groupRepository.delete(group);
    }

    @Transactional
    public void updateGroupCar(GroupEntity group, Car car) {
        group.setCar(car);
    }

    public GroupEntity findById(Long groupId) {
        return groupRepository.findById(groupId);
    }
}
