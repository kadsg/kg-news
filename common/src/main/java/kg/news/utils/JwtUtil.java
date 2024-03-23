package kg.news.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    /**
     * 生成token
     *
     * @param secretKey 秘钥
     * @param ttlMillis 过期时间
     * @param claims    信息载体
     * @return token
     */
    public static String generateToken(String secretKey, long ttlMillis, Map<String, ?> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ttlMillis);
        return JWT.create()
                .withPayload(claims)
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * 解析token
     *
     * @param secretKey 秘钥
     * @param token     token
     * @return 信息载体
     */
    public static Map<String, ?> verifyToken(String secretKey, String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaims();
    }

    /**
     * 处理带有 "Bearer " 前缀的令牌
     *
     * @param token 带有 "Bearer " 前缀的令牌
     * @return 没有前缀的令牌
     */
    public static String handleBearerPrefix(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
