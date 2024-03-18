//package gachicar.gachicarserver.config.jwt;
//
//import gachicar.gachicarserver.domain.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
///**
// * 유저의 정보를 가져오는 UserDetails 인터페이스를 상속하는 클래스
// * - Authentication을 담고 있음.
// */
//public class CustomUserDetail implements UserDetails {
//
//    private final User user;
//
//    public CustomUserDetail(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> user.getRole().getKey()); // key: ROLE_권한
//        return authorities;
//    }
//
//    public Long getId() {
//        return user.getId();
//    }
//
//    @Override
//    public String getPassword() {
//        return "";
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getEmail();
//    }
//
//    /* 세부 설정 */
//
//    @Override
//    public boolean isAccountNonExpired() {  // 계정의 만료 여부
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {   // 계정의 잠김 여부
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {  // 비밀번호 만료 여부
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {    // 계정의 활성화 여부
//        return true;
//    }
//}
