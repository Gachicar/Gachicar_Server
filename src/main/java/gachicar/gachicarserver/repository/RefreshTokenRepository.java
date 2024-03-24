package gachicar.gachicarserver.repository;


import gachicar.gachicarserver.domain.RefreshToken;
import gachicar.gachicarserver.exception.AuthErrorException;
import gachicar.gachicarserver.exception.AuthErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@ComponentScan
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /* refresh token 을 redis 에 저장 */
    public void save(RefreshToken refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getUserId());
        redisTemplate.expire(refreshToken.getRefreshToken(), 21L, TimeUnit.DAYS);
    }

    public RefreshToken findById(final String refreshToken) {

        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Long userId = valueOperations.get(refreshToken);

        if (Objects.isNull(userId)) {
            throw new AuthErrorException(AuthErrorStatus.REFRESH_EXPIRED);
        }
        return new RefreshToken(refreshToken, userId);
    }
}
