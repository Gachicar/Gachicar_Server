//package gachicar.gachicarserver.config.jwt;
//
//import gachicar.gachicarserver.domain.User;
//import gachicar.gachicarserver.exception.AuthErrorException;
//import gachicar.gachicarserver.exception.AuthErrorStatus;
//import gachicar.gachicarserver.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CustomUserDetailService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
//        User findUser = userRepository.findByEmail(email);
//        if (findUser == null) {
//            throw new AuthErrorException(AuthErrorStatus.GET_USER_FAILED);
//        } else {
//            return new CustomUserDetail(findUser);
//        }
//    }
//}
