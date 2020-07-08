package net.ys.mapper;

import net.ys.bean.EtlField;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlFieldMapper implements RowMapper<EtlField> {
    @Override
    public EtlField mapRow(ResultSet resultSet, int i) throws SQLException {
        EtlField etlField = new EtlField();
        etlField.setId(resultSet.getString("id"));
        etlField.setEntityId(resultSet.getString("entity_id"));
        etlField.setSrcFieldName(resultSet.getString("src_field_name"));
        etlField.setDesFieldName(resultSet.getString("des_field_name"));
        etlField.setCreateTime(resultSet.getLong("create_time"));
        return etlField;
    }
}