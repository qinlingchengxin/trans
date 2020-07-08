package net.ys.mapper;

import net.ys.bean.EtlAdmin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper implements RowMapper<EtlAdmin> {
	@Override
	public EtlAdmin mapRow(ResultSet resultSet, int i) throws SQLException {
		EtlAdmin admin = new EtlAdmin();
		admin.setId(resultSet.getString("id"));
		admin.setMagType(resultSet.getInt("mag_type"));
		admin.setUsername(resultSet.getString("username"));
		admin.setPassword(resultSet.getString("password"));
		return admin;
	}
}