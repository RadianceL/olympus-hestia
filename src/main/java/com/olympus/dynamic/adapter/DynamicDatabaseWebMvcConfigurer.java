package com.olympus.dynamic.adapter;

import com.olympus.dynamic.config.DynamicDatabaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author eddie.lys
 * @since 2024/4/2
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DynamicDatabaseWebMvcConfigurer implements WebMvcConfigurer {

    private final DynamicDatabaseConfiguration dynamicDatabaseConfiguration;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        DatabaseSelectorInterceptor interceptor = new DatabaseSelectorInterceptor(dynamicDatabaseConfiguration);
        registry.addInterceptor(interceptor).order(2).addPathPatterns("/**");
    }
}
