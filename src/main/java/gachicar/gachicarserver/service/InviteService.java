package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.GroupEntity;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.InviteResponse;
import gachicar.gachicarserver.dto.requestDto.AcceptInvitationRequestDto;
import gachicar.gachicarserver.dto.requestDto.InviteMemberRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final UserService userService;
    private final GroupService groupService;
    private final NotificationService notificationService;

    /**
     * 닉네임으로 멤버 초대 알림 보내기
     */
    public void inviteMemberByNickname(User manager, GroupEntity group, InviteMemberRequestDto inviteMemberRequestDto) {
        String memberNickname = inviteMemberRequestDto.getNickname();

        User user = userService.findByUserName(memberNickname);

        InviteResponse inviteResponse = InviteResponse.builder()
                .sender(manager.getName())
                .groupId(group.getGroupId())
                .build();

        notificationService.sendInvitation(user.getName(), inviteResponse);
    }

    /**
     * 초대 수락
     */
    @Transactional
    public void acceptInvitation(User user, AcceptInvitationRequestDto requestDto) {
        // 초대를 수락할 그룹
        Long groupId = requestDto.getGroupId();
        GroupEntity group = groupService.findById(groupId);

        // 초대 수락
        user.setGroup(group);
        List<User> memberList = group.getMemberList();
        if (!memberList.contains(user)) { // 중복 체크
            memberList.add(user);
            group.setMemberList(memberList);
        } else {
            throw new ApiErrorException(ApiErrorStatus.ALREADY_MEMBER);
        }

        InviteResponse inviteResponse = InviteResponse.builder()
                .sender(user.getName())
                .groupId(groupId)
                .build();

        // 초대 수락 알림 보내기
        notificationService.sendAcceptInvitation(group.getManager().getName(), inviteResponse);
    }

}
