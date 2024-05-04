package kg.news.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PageHelper 分页配置
 *
 * @author pjy
 * @date 2021年07月29日
 */
@Configuration
public class PageHelperConfiguration {

    @Bean
    public Interceptor[] plugins() {
        return new Interceptor[] { new PageInterceptor() };
    }
}