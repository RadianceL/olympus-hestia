package com.olympus.dynamic.adapter;

import com.olympus.dynamic.core.DatasourceSelectorHolder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器
 * @author eddie
 * @since 2022/7/20
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpCountrySelectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response,@Nonnull Object handler) {
        String countryCode = request.getHeader(RequestBasicHeader.CONSTITUTIONAL_CODE);
        if (StringUtils.isNotBlank(countryCode)) {
            DatasourceSelectorHolder.setCurrentDatabase(countryCode);
        }
        return true;
    }
}
