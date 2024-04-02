package com.olympus.dynamic.core;

import jakarta.annotation.Nonnull;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据库选择器
 *
 * @author eddie.lys
 * @since 12/20/2021
 */
public class DatabaseRouting extends AbstractRoutingDataSource {

    private final AtomicBoolean initializationFlag = new AtomicBoolean(false);

    private final DatasourceRegister dataSourceRegister;

    public DatabaseRouting(DatasourceRegister dataSourceRegister) {
        this.dataSourceRegister = dataSourceRegister;
    }

    /**
     * mybatis在使用mapper接口执行sql的时候会从该方法获取connection执行sql
     * 如果事务是spring或者mybatis在管理，那么直接返回原生的connection
     * 如果是我们自己控制事务，则返回我们自己实现的ConnetWarp
     *
     * @return Connection
     * @throws SQLException SQLException
     */
    @Override
    public @Nonnull Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DatasourceSelectorHolder.getCurrentDatabase();
    }

    @Override
    protected @Nonnull DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    @Override
    public void afterPropertiesSet() {
        if (initializationFlag.compareAndSet(false, true)) {
            super.setTargetDataSources(dataSourceRegister.getDataSourceMap());
            super.afterPropertiesSet();
        }
    }
}
