package pl.robertprogramista.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration for interceptors
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final List<HandlerInterceptor> interceptors;

    InterceptorConfig(List<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * Registers interceptors
     * @param registry interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptors.forEach(registry::addInterceptor);
    }
}
