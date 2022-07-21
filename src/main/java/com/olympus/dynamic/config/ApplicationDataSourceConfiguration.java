package com.olympus.dynamic.config;

import com.olympus.dynamic.core.DatabaseSelector;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 *
 * @author eddie.lys
 * @since 2022/7/20
 */
public class ApplicationDataSourceConfiguration {

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix ="spring.datasource")
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.type(DatabaseSelector.class);
        return dataSourceBuilder.build();
    }
}
