package com.el.dynamic.core;

import com.el.dynamic.datasource.ConnectWarp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据库选择器
 *
 * @author eddie.lys
 * @since 12/20/2021
 */
public class DatabaseSelector extends AbstractRoutingDataSource {
    /**
     * 保存当前线程使用了事务的数据库连接(connection)
     * 当我们自己管理事务的时候即可从此处获取到当前线程使用了哪些连接从而让这些被使用的连接commit/rollback/close
     */
    private final ThreadLocal<Map<String, ConnectWarp>> connectionThreadLocal = new ThreadLocal<>();

    private final AtomicBoolean initializationFlag = new AtomicBoolean(false);

    @Autowired
    private DatasourceRegister dataSourceRegister;

    /**
     * mybatis在使用mapper接口执行sql的时候会从该方法获取connection执行sql
     * 如果事务是spring或者mybatis在管理，那么直接返回原生的connection
     * 如果是我们自己控制事务，则返回我们自己实现的ConnetWarp
     *
     * @return Connection
     * @throws SQLException SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        Map<String, ConnectWarp> stringConnectionMap = connectionThreadLocal.get();
        if (stringConnectionMap == null) {
            // 没开事物 直接返回
            return determineTargetDataSource().getConnection();
        } else {
            // 开了事物 从当前线程中拿 而且拿到的是 包装过的connect 只有手动去提交和关闭连接
            String currentName = (String) determineCurrentLookupKey();
            return stringConnectionMap.get(currentName);
        }
    }

    /**
     * 开启事物的时候,把连接放入 线程中,后续crud 都会拿对应的连接操作
     *
     * @param key      事务的key
     * @param connection 连接
     */
    public void bindConnection(String key, Connection connection) {
        Map<String, ConnectWarp> connectionMap = connectionThreadLocal.get();
        if (connectionMap == null) {
            connectionMap = new HashMap<>();
            connectionThreadLocal.set(connectionMap);
        }
        ConnectWarp connectWarp = new ConnectWarp(connection);
        connectionMap.put(key, connectWarp);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DatasourceSelectorHolder.getCurrentDatabase();
    }

    @Override
    protected DataSource determineTargetDataSource() {
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
