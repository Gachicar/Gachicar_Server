package gachicar.gachicarserver.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import gachicar.gachicarserver.dto.ResultDto;
import gachicar.gachicarserver.exception.AuthErrorException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //JwtFilter 를 호출하는데, 이 필터에서 JwtErrorException 이 떨어진다.
            filterChain.doFilter(request, response);
        } catch(AuthErrorException e) {
            errorResponse(response, e);
        }
    }

    private static void errorResponse(HttpServletResponse response, AuthErrorException e) throws IOException {
        // response 객체를 사용하여 HTTP 응답을 설정
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 상태 코드 설정 (예: 200 OK)
        response.setContentType("application/json"); // 응답 형식 설정 (JSON 등)
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정

        // 응답 본문 작성
        ResultDto<Object> responseBody = ResultDto.of(e.getCode(), e.getErrorMsg(), null);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseBody);
    }
}
