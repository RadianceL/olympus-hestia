package com.olympus.dynamic.config;

import com.olympus.dynamic.core.DatabaseRouting;
import com.olympus.dynamic.core.DatasourceRegister;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
        dataSourceBuilder.type(DatabaseRouting.class);
        return dataSourceBuilder.build();
    }

    @Bean("datasourceRegister")
    @ConditionalOnClass(HikariConfig.class)
    public DatasourceRegister datasourceRegister(DynamicDatabaseConfiguration dynamicDatabaseConfiguration) {
        return new DatasourceRegister(dynamicDatabaseConfiguration);
    }

}
