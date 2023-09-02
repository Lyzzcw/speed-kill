package lyzzcw.work.speedkill.stock.starter.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/7 13:45
 * Description: No Description
 */
@Configuration
public class CustomizeAdapter implements WebMvcConfigurer {

    @Autowired
    private CustomizeInterceptor customizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customizeInterceptor).addPathPatterns("/**").excludePathPatterns("/user/doLogin");
    }

}
