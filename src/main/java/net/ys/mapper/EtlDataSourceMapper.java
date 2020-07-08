package net.ys.mapper;

import net.ys.bean.EtlDataSource;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlDataSourceMapper implements RowMapper<EtlDataSource> {
    @Override
    public EtlDataSource mapRow(ResultSet resultSet, int i) throws SQLException {
        EtlDataSource dataSource = new EtlDataSource();
        dataSource.setId(resultSet.getString("id"));
        dataSource.setSourceName(resultSet.getString("source_name"));
        dataSource.setDbType(resultSet.getInt("db_type"));
        dataSource.setDbIp(resultSet.getString("db_ip"));
        dataSource.setDbPort(resultSet.getInt("db_port"));
        dataSource.setDbName(resultSet.getString("db_name"));
        dataSource.setDbUsername(resultSet.getString("db_user_name"));
        dataSource.setDbPwd(resultSet.getString("db_pwd"));
        dataSource.setAlive(resultSet.getInt("alive"));
        dataSource.setCreateTime(resultSet.getLong("create_time"));
        dataSource.setStatus(resultSet.getInt("status"));
        dataSource.setDbSchema(resultSet.getString("db_schema"));
        return dataSource;
    }
}