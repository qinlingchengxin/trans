package net.ys.mapper;

import net.ys.bean.EtlProject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlProjectMapper implements RowMapper<EtlProject> {
    @Override
    public EtlProject mapRow(ResultSet resultSet, int i) throws SQLException {
        EtlProject etlProject = new EtlProject();
        etlProject.setId(resultSet.getString("id"));
        etlProject.setPrjName(resultSet.getString("prj_name"));

        etlProject.setSrcDbId(resultSet.getString("src_db_id"));
        etlProject.setSrcDbName(resultSet.getString("src_db_name"));
        etlProject.setDesDbId(resultSet.getString("des_db_id"));
        etlProject.setDesDbName(resultSet.getString("des_db_name"));

        etlProject.setCenterDbType(resultSet.getInt("center_db_type"));
        etlProject.setCenterDbIp(resultSet.getString("center_db_ip"));
        etlProject.setCenterDbPort(resultSet.getString("center_db_port"));
        etlProject.setCenterDbName(resultSet.getString("center_db_name"));
        etlProject.setCenterDbUsername(resultSet.getString("center_db_user_name"));
        etlProject.setCenterDbPwd(resultSet.getString("center_db_pwd"));

        etlProject.setBusDbType(resultSet.getInt("bus_db_type"));
        etlProject.setBusDbIp(resultSet.getString("bus_db_ip"));
        etlProject.setBusDbPort(resultSet.getString("bus_db_port"));
        etlProject.setBusDbName(resultSet.getString("bus_db_name"));
        etlProject.setBusDbUsername(resultSet.getString("bus_db_user_name"));
        etlProject.setBusDbPwd(resultSet.getString("bus_db_pwd"));

        etlProject.setCreateTime(resultSet.getLong("create_time"));
        return etlProject;
    }
}