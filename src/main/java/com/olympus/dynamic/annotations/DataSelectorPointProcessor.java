package com.olympus.dynamic.annotations;

import com.olympus.dynamic.core.DatasourceSelectorHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * 数据库选择注解处理器
 *
 * @author eddie.lys
 * @since 2024/4/18
 */
@Slf4j
@Aspect
public class DataSelectorPointProcessor implements Ordered {

    @Pointcut("@annotation(com.olympus.dynamic.annotations.DataSelectorPoint)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSelectorPoint ds = method.getAnnotation(DataSelectorPoint.class);
        //如果未指定数据源就使用第一个数据源
        if(ds == null){
            DatasourceSelectorHolder.setCurrentDatabase("default");
            log.debug("set datasource is default");
        }else {
            DatasourceSelectorHolder.setCurrentDatabase(ds.datasource());
            log.debug("set datasource is {}", ds.datasource());
        }
        try {
            return point.proceed();
        } finally {
            DatasourceSelectorHolder.clear();
            log.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
