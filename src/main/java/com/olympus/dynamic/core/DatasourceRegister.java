package com.olympus.dynamic.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.olympus.dynamic.config.DynamicDatabaseConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 数据源注册器
 * since 4/26/22
 *
 * @author eddie
 */
public class DatasourceRegister {
    /**
     * 动态数据库配置
     */
    private final DynamicDatabaseConfiguration dynamicDatabaseConfiguration;

    public DatasourceRegister(DynamicDatabaseConfiguration dynamicDatabaseConfiguration) {
        this.dynamicDatabaseConfiguration = dynamicDatabaseConfiguration;
    }

    public Map<Object, Object> getDataSourceMap() {
        Map<String, Map<String, String>> databaseConnectionConfig = dynamicDatabaseConfiguration.getDatabaseConnectionConfig();
        if (CollectionUtils.isEmpty(databaseConnectionConfig)) {
            return new HashMap<>();
        }
        Map<Object, Object> dataSourceMap = new HashMap<>(8);
        AtomicReference<Boolean> isFirst = new AtomicReference<>(true);
        databaseConnectionConfig.forEach((poolName, dataSourceConfigMap) -> {
            HikariDataSource dataSource = new HikariDataSource(getHikariConfig(poolName, dataSourceConfigMap));
            dataSourceMap.put(poolName, dataSource);
            if (isFirst.get()) {
                dataSourceMap.put(DatasourceSelectorHolder.DEFAULT_DATABASE, dataSource);
                isFirst.set(false);
            }
        });
        return dataSourceMap;
    }

    private HikariConfig getHikariConfig(String poolName, Map<String, String> dataSourceConfigMap) {
        HikariConfig hikariConfig = new HikariConfig();
        String url = dataSourceConfigMap.get("url");
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("data template [" + poolName + "] URL can`t be null");
        }
        hikariConfig.setJdbcUrl(url);

        String username = dataSourceConfigMap.get("username");
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("data template [" + poolName + "] username can`t be null");
        }
        hikariConfig.setUsername(username);

        String password = dataSourceConfigMap.get("password");
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("data template [" + poolName + "] password can`t be null");
        }
        hikariConfig.setPassword(password);

        // 设置连接名称
        hikariConfig.setPoolName(poolName);

//        String minimumIdle = dataSourceConfigMap.get("minimumIdle");
//        if (StringUtils.isBlank(minimumIdle)) {
//            minimumIdle = "5";
//        }
//        hikariConfig.setMinimumIdle(Integer.parseInt(minimumIdle));

        String maximumPoolSize = dataSourceConfigMap.get("maximumPoolSize");
        if (StringUtils.isBlank(maximumPoolSize)) {
            maximumPoolSize = "10";
        }
        hikariConfig.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));

        String connectionTimeout = dataSourceConfigMap.get("connectionTimeout");
        if (StringUtils.isBlank(connectionTimeout)) {
            connectionTimeout = "30000";
        }
        hikariConfig.setConnectionTimeout(Long.parseLong(connectionTimeout));

        String idleTimeout = dataSourceConfigMap.get("idleTimeout");
        if (StringUtils.isBlank(idleTimeout)) {
            idleTimeout = "30000";
        }
        hikariConfig.setIdleTimeout(Long.parseLong(idleTimeout));

        String autoCommit = dataSourceConfigMap.get("autoCommit");
        if (StringUtils.isBlank(autoCommit)) {
            autoCommit = "true";
        }
        hikariConfig.setAutoCommit(Boolean.parseBoolean(autoCommit));

        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setConnectionTestQuery("SELECT 1");
        return hikariConfig;
    }

}
