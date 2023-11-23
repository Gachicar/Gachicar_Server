package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Group;
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

    @Transactional
    /* 회원 닉네임 변경 */
    public void update(Long userId, UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        User user = userRepository.findOne(userId);
        user.setName(updateUserNicknameRequestDto.getUserNickname());
    }

    @Transactional
    /* 회원 탈퇴 */
    public void delete(Long userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(user);
    }

    @Transactional
    /* 그룹 변경 */
    public void updateGroup(User user, Group group) {
        user.setGroup(group);
        user.setRole(Role.MANAGER);
    }

    @Transactional
    /* 그룹 삭제 */
    public void deleteGroup(User user, DeleteGroupRequestDto requestDto) {
        Group group = user.getGroup();

        if (group != null) {
            if (Objects.equals(group.getGroupId(), requestDto.getDeleteId())) {
                // 사용자가 그룹장인지 확인
                if (group.getManager() == user) {
                    user.setRole(Role.USER);
                    user.setGroup(null);
                } else {
                    throw new ApiErrorException(ApiErrorStatus.NOT_MANAGER);
                }
            }
        } else {
            throw new ApiErrorException(ApiErrorStatus.NOT_HAVE_GROUP);
        }
    }
}
