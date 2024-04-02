package com.olympus.dynamic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 动态数据配置
 * since 5/11/22
 *
 * @author eddie
 */
@Data
@Component
@ConfigurationProperties("spring.datasource.dynamic")
public class DynamicDatabaseConfiguration {
    /**
     * 动态数据库headerKey
     */
    private String dynamicDatabaseHeaderKey;
    /**
     * 数据库链接配置
     */
    private Map<String, Map<String, String>> databaseConnectionConfig;

}
