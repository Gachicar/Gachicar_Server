package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.UpdateUserNicknameRequestDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.AuthErrorStatus;
import gachicar.gachicarserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
