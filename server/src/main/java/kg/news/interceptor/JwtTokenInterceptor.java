package kg.news.interceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.news.context.BaseContext;
import kg.news.properties.JwtProperties;
import kg.news.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;

    public JwtTokenInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        // 判断当前拦截到的是Controller的方法还是其他资源，如果是其他资源，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());
        token = JwtUtil.handleBearerPrefix(token);
        // 校验令牌
        log.info("jwt校验:{}", token);
        Map<String, ?> verifyToken = JwtUtil.verifyToken(jwtProperties.getSecretKey(), token);
        if (CollectionUtils.isEmpty(verifyToken)) {
            response.setStatus(401);
            return false;
        }
        String id = verifyToken.get("id").toString();
        BaseContext.setCurrentId(Long.valueOf(id));
        log.info("当前用户id：{}", id);
        return true;
    }

    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        BaseContext.remove();
    }
}
