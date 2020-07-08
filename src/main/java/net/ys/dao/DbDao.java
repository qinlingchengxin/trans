package net.ys.dao;

import net.ys.bean.DbField;
import net.ys.bean.DbTable;
import net.ys.bean.EtlAllTable;
import net.ys.bean.EtlDataSource;
import net.ys.mapper.EtlAllTableMapper;
import net.ys.mapper.EtlDataSourceMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class DbDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<EtlDataSource> queryDataSources() {
        String sql = "SELECT `id`, `source_name`, `db_schema`, `db_type`, `db_ip`, `db_port`, `db_name`, `db_user_name`, `db_pwd`, `alive`, `create_time`, `status` FROM sys_etl_data_source WHERE `status` = 1 AND alive = 1";
        return jdbcTemplate.query(sql, new EtlDataSourceMapper());
    }

    public void addTables(final String dsId, final List<DbTable> tables) {
        String sql = "INSERT IGNORE INTO sys_etl_all_table ( id, ds_id, `name`, comment, create_time ) VALUES (?,?,?,?,?)";
        final long now = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, dsId);
                ps.setString(3, tables.get(i).getTableName());
                ps.setString(4, tables.get(i).getComment());
                ps.setLong(5, now);
            }

            @Override
            public int getBatchSize() {
                return tables.size();
            }
        });
    }

    public List<EtlAllTable> queryEtlAllTables(EtlDataSource dataSource) {
        String sql = "SELECT `id`, `ds_id`, `name`, `comment`, `create_time` FROM sys_etl_all_table WHERE `ds_id` = ?";
        return jdbcTemplate.query(sql, new EtlAllTableMapper(), dataSource.getId());
    }

    public void addFields(final List<DbField> fields) {
        final long now = System.currentTimeMillis();
        String sql = "INSERT IGNORE INTO sys_etl_all_field ( `id`, `table_id`, `name`, `comment`, `pri_key`, `create_time`) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DbField field = fields.get(i);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, field.getTableId());
                ps.setString(3, field.getFieldName());
                ps.setString(4, field.getComment());
                ps.setInt(5, field.getPriKey());
                ps.setLong(6, now);
            }

            @Override
            public int getBatchSize() {
                return fields.size();
            }
        });
    }
}
