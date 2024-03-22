package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.requestDto.InviteMemberRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final UserRepository userRepository;

    public void inviteMemberByNickname(Group group, InviteMemberRequestDto inviteMemberRequestDto) {
        String nickname = inviteMemberRequestDto.getNickname();
        User member = findUserByNickname(nickname);
        // TODO: 멤버에게 초대 알림 보내기
    }

    /**
     * 초대 수락
     */
    @Transactional
    public void allowInvitation(User user, Group group) {
        user.setGroup(group);
        List<User> memberList = group.getMemberList();
        memberList.add(user);
        group.setMemberList(memberList);
        // TODO: 그룹장에게 알림 보내기
    }

    private User findUserByNickname(String nickname) {
        try {
            List<User> userList = userRepository.findByName(nickname);
            return userList.get(1);
        } catch (Exception e) {
            throw new ApiErrorException(ApiErrorStatus.NOT_EXIST_NAME);
        }
    }
}
