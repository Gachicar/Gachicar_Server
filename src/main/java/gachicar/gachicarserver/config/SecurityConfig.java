//package gachicar.gachicarserver.config;
//
//import gachicar.gachicarserver.config.jwt.JwtExceptionFilter;
//import gachicar.gachicarserver.config.jwt.JwtRequestFilter;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@Slf4j
//public class SecurityConfig {
//
//    private final JwtRequestFilter jwtRequestFilter;
//    private final JwtExceptionFilter jwtExceptionFilter;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//
//                /* 컨트롤러 수행 전에 jwt 토큰 검사 먼저 진행 */
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtExceptionFilter, JwtRequestFilter.class)
//
//                /* 세션 사용하지 않음 */
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                /*
//                 * HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
//                 * - 회원가입/로그인 또는 토큰 재발급 요청 시 모든 접근 허용
//                 */
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/auth/social").permitAll()    // 모든 접근 허용
//                .antMatchers("/api/auth/newToken").permitAll()
//                .antMatchers("/api/group").permitAll()
////                .antMatchers("/api/group/**").permitAll()
//                .antMatchers("/api/**").authenticated()     // 인증만 되면 들어갈 수 있는 주소
//                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")   // 관리자만 접속 가능한 주소
//                .anyRequest().authenticated()  // 이외에는 접근 권한 필요
//                .and().build();
//    }
//}
