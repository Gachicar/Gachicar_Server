package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.GroupEntity;
import gachicar.gachicarserver.domain.Role;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.requestDto.DeleteGroupRequestDto;
import gachicar.gachicarserver.dto.requestDto.UpdateUserNicknameRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.AuthErrorStatus;
import gachicar.gachicarserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원 조회 */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        try {
            return userRepository.findOne(userId);
        } catch (Exception e) {
            throw new AuthErrorException(AuthErrorStatus.GET_USER_FAILED);
        }
    }

    public User findByUserName(String nickname) {
        List<User> userList = userRepository.findByName(nickname);
        if (userList.isEmpty()) {
            throw new AuthErrorException(AuthErrorStatus.GET_USER_FAILED);
        }
        return userList.get(0); // 리스트가 비어있지 않으면 첫 번째 요소를 반환
    }

    @Transactional
    /* 회원 닉네임 변경 */
    public void update(Long userId, UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        String newName = updateUserNicknameRequestDto.getUserNickname();
        if (userRepository.findByName(newName).isEmpty()) {
            User user = userRepository.findOne(userId);
            user.setName(newName);
        } else {
            throw new ApiErrorException(ApiErrorStatus.DUPLICATED_USER_NAME);
        }
    }

    @Transactional
    /* 회원 탈퇴 */
    public void delete(Long userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(user);
    }

    @Transactional
    /* 그룹 변경 */
    public void updateGroup(User user, GroupEntity group) {
        user.setGroup(group);
        user.setRole(Role.MANAGER);
        List<User> memberList = group.getMemberList();
        memberList.add(user);
        group.setMemberList(memberList);
    }

    @Transactional
    /* 그룹 삭제 */
    public void deleteGroup(User user, DeleteGroupRequestDto requestDto) {
        GroupEntity group = user.getGroup();

        if (group != null) {
            if (requestDto.getDeleteId() != null) {
                if (Objects.equals(group.getGroupId(), requestDto.getDeleteId())) {
                    // 사용자가 그룹장인지 확인
                    if (group.getManager() == user) {
                        user.setRole(Role.USER);
                        user.setGroup(null);
                    } else {
                        throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
                    }
                }
            } else
                throw new ApiErrorException(ApiErrorStatus.MALFORMED);
        } else {
            throw new ApiErrorException(ApiErrorStatus.NOT_HAVE_GROUP);
        }
    }
}
