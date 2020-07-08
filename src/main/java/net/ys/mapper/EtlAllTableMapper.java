package net.ys.mapper;

import net.ys.bean.EtlAllTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlAllTableMapper implements RowMapper<EtlAllTable> {
    @Override
    public EtlAllTable mapRow(ResultSet resultSet, int i) throws SQLException {
        EtlAllTable table = new EtlAllTable();
        table.setId(resultSet.getString("id"));
        table.setDsId(resultSet.getString("ds_id"));
        table.setName(resultSet.getString("name"));
        table.setComment(resultSet.getString("comment"));
        table.setCreateTime(resultSet.getLong("create_time"));
        return table;
    }
}