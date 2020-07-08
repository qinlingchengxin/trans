package net.ys.mapper;

import net.ys.bean.EtlAllField;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlAllFieldMapper implements RowMapper<EtlAllField> {
    @Override
    public EtlAllField mapRow(ResultSet resultSet, int i) throws SQLException {
        EtlAllField field = new EtlAllField();
        field.setId(resultSet.getString("id"));
        field.setTableId(resultSet.getString("table_id"));
        field.setName(resultSet.getString("name"));
        field.setPriKey(resultSet.getInt("pri_key"));
        field.setComment(resultSet.getString("comment"));
        field.setCreateTime(resultSet.getLong("create_time"));
        return field;
    }
}