package com.el.dynamic.adapter;

import com.el.dynamic.core.DatasourceSelectorHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * @author eddie
 * @since 2022/7/20
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpCountrySelectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String countryCode = request.getHeader(RequestBasicHeader.CONSTITUTIONAL_CODE);
        if (StringUtils.isNotBlank(countryCode)) {
            DatasourceSelectorHolder.setCurrentCountry(countryCode);
        }
        return true;
    }

    /**
     * 处理后调用（正常）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * 处理后调用(任何情况)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        DatasourceSelectorHolder.clear();
    }
}
