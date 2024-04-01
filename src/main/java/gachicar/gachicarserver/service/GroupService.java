package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.GroupEntity;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.GroupDto;
import gachicar.gachicarserver.dto.requestDto.*;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

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
        GroupEntity group = user.getGroup();
        if (group != null) {
            return new GroupDto(user.getGroup());
        } else {
            throw new ApiErrorException(ApiErrorStatus.NOT_HAVE_GROUP);
        }
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


    /**
     * 그룹에서 멤버 삭제
     */
    @Transactional
    public void deleteMemberFromGroup(User user, InviteOrRemoveMemberRequestDto requestDto) {
        GroupEntity group = user.getGroup();

        // 사용자가 그룹장인지 확인
        if (group.getManager() == user) {
            String nickname = requestDto.getNickname();
            User byUserName = userService.findByUserName(nickname);
            List<User> memberList = group.getMemberList();
            memberList.remove(byUserName);
            group.setMemberList(memberList);
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
