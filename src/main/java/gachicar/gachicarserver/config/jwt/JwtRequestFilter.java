//package gachicar.gachicarserver.config.jwt;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private final JwtProvider jwtProvider;
//    private static final String BEARER_PREFIX = "Bearer ";
//
//    /**
//     * SecurityContext에 Access Token으로부터 뽑아온 인증 정보를 저장하는 메서드
//     *
//     *  doFilterInternal() 내부에서 수행하는 작업:
//     *  – HTTP 쿠키 or 헤더에서 JWT 가져오기
//     *  – 요청에 JWT가 있으면 유효성을 검사하고 사용자 이름을 구문 분석한다.
//     *  – 사용자 이름에서 UserDetails를 가져와 인증 개체를 만든다.
//     *  – setAuthentication(authentication) 메서드를 사용하여 SecurityContext에서 현재 UserDetails를 설정한다.
//     *
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        // "/api/auth/social" 경로로의 요청은 필터를 실행하지 않도록 걸러낸다.
//        if (!request.getRequestURI().equals("/api/auth/social") && !request.getRequestURI().equals("/api/auth/newToken")) {
//            // request: 헤더에서 넘어오는 JWT
//            String jwt = resolveToken(request);
//            log.info("jwt token = {}", jwt);
//
//            // token 검사
//            if (jwtProvider.validateToken(jwt)) {
//                Authentication authentication = jwtProvider.getAuthentication(jwt);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    /**
//     * Header에서 토큰값 추출하는 메서드
//     */
//    private String resolveToken(HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
//            return token.substring(7);  // "Bearer "를 제외한 토큰 값
//        }
//        return null;
//    }
//}
