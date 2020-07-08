package net.ys.dao;

import net.ys.bean.*;
import net.ys.constant.X;
import net.ys.mapper.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class EtlDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public long queryEtlEntityCount(String prjId) {
        String sql = "SELECT COUNT(*) FROM sys_etl_entity WHERE prj_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, prjId);
    }

    public List<EtlEntity> queryEtlEntities(String prjId, int page, int pageSize) {
        String sql = "SELECT id, prj_id, src_tab_id, src_tab_name, des_tab_id, des_tab_name, src_primary_key, des_primary_key, description, etl_id, create_time, `repeat`, schedule_type, interval_second, interval_minute, fixed_hour, fixed_minute, fixed_weekday, fixed_day, is_exec, is_api_exec, last_trans_time, `condition` FROM sys_etl_entity WHERE prj_id = ? ORDER BY src_tab_name LIMIT ?,?";
        return jdbcTemplate.query(sql, new EtlEntityMapper(), prjId, (page - 1) * pageSize, pageSize);
    }

    public EtlEntity queryEtlEntity(String id) {
        String sql = "SELECT id, prj_id, src_tab_id, src_tab_name, des_tab_id, des_tab_name, src_primary_key, des_primary_key, description, etl_id, create_time, `repeat`, schedule_type, interval_second, interval_minute, fixed_hour, fixed_minute, fixed_weekday, fixed_day, is_exec, is_api_exec, last_trans_time, `condition` FROM sys_etl_entity WHERE id = ?";
        List<EtlEntity> list = jdbcTemplate.query(sql, new EtlEntityMapper(), id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public boolean updateEtlEntity(EtlEntity etlEntity) {
        String sql = "UPDATE sys_etl_entity SET src_tab_id = ?, src_tab_name = ?, des_tab_id = ?, des_tab_name = ?, src_primary_key = ?, des_primary_key = ?, description = ?, `repeat` = ?, schedule_type = ?, interval_second = ?, interval_minute = ?, fixed_hour = ?, fixed_minute = ?, fixed_weekday = ?, fixed_day = ? WHERE id = ?";
        return jdbcTemplate.update(sql, etlEntity.getSrcTabId(), etlEntity.getSrcTabName(), etlEntity.getDesTabId(), etlEntity.getDesTabName(), etlEntity.getSrcPrimaryKey(), etlEntity.getDesPrimaryKey(), etlEntity.getDescription(), etlEntity.getRepeat(), etlEntity.getScheduleType(), etlEntity.getIntervalSecond(), etlEntity.getIntervalMinute(), etlEntity.getFixedHour(), etlEntity.getFixedMinute(), etlEntity.getFixedWeekday(), etlEntity.getFixedDay(), etlEntity.getId()) >= 0;
    }

    public EtlEntity addEtlEntity(EtlEntity etlEntity) {
        etlEntity.setId(UUID.randomUUID().toString());
        etlEntity.setEtlId(String.valueOf(System.currentTimeMillis()));
        String sql = "INSERT INTO sys_etl_entity ( id, prj_id, src_tab_id, src_tab_name, des_tab_id, des_tab_name, src_primary_key, des_primary_key, description, etl_id, create_time, `repeat`, schedule_type, interval_second, interval_minute, fixed_hour, fixed_minute, fixed_weekday, fixed_day  ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean flag = jdbcTemplate.update(sql, etlEntity.getId(), etlEntity.getPrjId(), etlEntity.getSrcTabId(), etlEntity.getSrcTabName(), etlEntity.getDesTabId(), etlEntity.getDesTabName(), etlEntity.getSrcPrimaryKey(), etlEntity.getDesPrimaryKey(), etlEntity.getDescription(), etlEntity.getEtlId(), System.currentTimeMillis(), etlEntity.getRepeat(), etlEntity.getScheduleType(), etlEntity.getIntervalSecond(), etlEntity.getIntervalMinute(), etlEntity.getFixedHour(), etlEntity.getFixedMinute(), etlEntity.getFixedWeekday(), etlEntity.getFixedDay()) > 0;
        if (flag) {
            return etlEntity;
        }
        return null;
    }

    public long queryEtlFieldCount(String entityId) {
        String sql = "SELECT COUNT(*) FROM sys_etl_field WHERE entity_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, entityId);
    }

    public List<EtlField> queryEtlFields(String entityId, int page, int pageSize) {
        String sql = "SELECT id, entity_id, src_field_name, des_field_name, create_time FROM sys_etl_field WHERE entity_id = ? ORDER BY src_field_name LIMIT ?,?";
        return jdbcTemplate.query(sql, new EtlFieldMapper(), entityId, (page - 1) * pageSize, pageSize);
    }

    public List<EtlField> queryEtlKtrFields(String entityId) {
        String sql = "SELECT id, entity_id, src_field_name, des_field_name, create_time FROM sys_etl_field ef WHERE ef.entity_id = ? ORDER BY create_time";
        return jdbcTemplate.query(sql, new EtlFieldMapper(), entityId);
    }

    public EtlField addEtlField(EtlField etlField) {
        etlField.setId(UUID.randomUUID().toString());
        String sql = "INSERT IGNORE INTO sys_etl_field ( id, entity_id, src_field_name, des_field_name, create_time ) VALUES (?,?,?,?,?)";
        boolean flag = jdbcTemplate.update(sql, etlField.getId(), etlField.getEntityId(), etlField.getSrcFieldName(), etlField.getDesFieldName(), System.currentTimeMillis()) >= 0;

        if (flag) {
            return etlField;
        }

        return null;
    }

    public long queryEtlProjectCount() {
        String sql = "SELECT COUNT(*) FROM sys_etl_project WHERE status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<EtlProject> queryEtlProjects(int page, int pageSize) {
        String sql = "SELECT ep.id, ep.prj_name, src_db.id AS src_db_id, src_db.source_name AS src_db_name, des_db.id AS des_db_id, des_db.source_name AS des_db_name, src_db.db_type AS center_db_type, src_db.db_ip AS center_db_ip, src_db.db_name AS center_db_name, src_db.db_port AS center_db_port, src_db.db_user_name AS center_db_user_name, src_db.db_pwd AS center_db_pwd, des_db.db_type AS bus_db_type, des_db.db_ip AS bus_db_ip, des_db.db_name AS bus_db_name, des_db.db_port AS bus_db_port, des_db.db_user_name AS bus_db_user_name, des_db.db_pwd AS bus_db_pwd, ep.create_time FROM sys_etl_project ep LEFT JOIN sys_etl_data_source src_db ON src_db.id = ep.src_db_id LEFT JOIN sys_etl_data_source des_db ON des_db.id = ep.des_db_id WHERE ep. STATUS = 0 LIMIT ?,?";
        return jdbcTemplate.query(sql, new EtlProjectMapper(), (page - 1) * pageSize, pageSize);
    }

    public EtlProject queryEtlProject(String id) {
        String sql = "SELECT ep.id, ep.prj_name, src_db.id AS src_db_id, src_db.source_name AS src_db_name, des_db.id AS des_db_id, des_db.source_name AS des_db_name, src_db.db_type AS center_db_type, src_db.db_ip AS center_db_ip, src_db.db_name AS center_db_name, src_db.db_port AS center_db_port, src_db.db_user_name AS center_db_user_name, src_db.db_pwd AS center_db_pwd, des_db.db_type AS bus_db_type, des_db.db_ip AS bus_db_ip, des_db.db_name AS bus_db_name, des_db.db_port AS bus_db_port, des_db.db_user_name AS bus_db_user_name, des_db.db_pwd AS bus_db_pwd, ep.create_time FROM sys_etl_project ep, sys_etl_data_source src_db, sys_etl_data_source des_db WHERE src_db.id = ep.src_db_id AND des_db.id = ep.des_db_id AND ep.id = ?";
        List<EtlProject> list = jdbcTemplate.query(sql, new EtlProjectMapper(), id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public boolean updateEtlProject(EtlProject etlProject) {
        String sql = "UPDATE sys_etl_project SET prj_name = ?, src_db_id = ?, des_db_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql, etlProject.getPrjName(), etlProject.getSrcDbId(), etlProject.getDesDbId(), etlProject.getId()) >= 0;
    }

    public EtlProject addEtlProject(EtlProject etlProject) {
        etlProject.setId(UUID.randomUUID().toString());
        String sql = "INSERT INTO sys_etl_project ( id, prj_name, src_db_id, des_db_id, create_time ) VALUES ( ?,?,?,?,? )";
        boolean flag = jdbcTemplate.update(sql, etlProject.getId(), etlProject.getPrjName(), etlProject.getSrcDbId(), etlProject.getDesDbId(), System.currentTimeMillis()) > 0;
        if (flag) {
            return etlProject;
        }
        return null;
    }

    public EtlProject queryEtlKtrProject(String entityId) {
        String sql = "SELECT ep.id, ep.prj_name, src_db.id AS src_db_id, src_db.source_name AS src_db_name, des_db.id AS des_db_id, des_db.source_name AS des_db_name, src_db.db_type AS center_db_type, src_db.db_ip AS center_db_ip, src_db.db_name AS center_db_name, src_db.db_port AS center_db_port, src_db.db_user_name AS center_db_user_name, src_db.db_pwd AS center_db_pwd, des_db.db_type AS bus_db_type, des_db.db_ip AS bus_db_ip, des_db.db_name AS bus_db_name, des_db.db_port AS bus_db_port, des_db.db_user_name AS bus_db_user_name, des_db.db_pwd AS bus_db_pwd, ep.create_time FROM sys_etl_project ep, sys_etl_data_source src_db, sys_etl_data_source des_db, sys_etl_entity ee WHERE src_db.id = ep.src_db_id AND des_db.id = ep.des_db_id AND ep.id = ee.prj_id AND ee.id = ?";
        List<EtlProject> list = jdbcTemplate.query(sql, new EtlProjectMapper(), entityId);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public long queryEtlDataSourceCount() {
        String sql = "SELECT COUNT(*) FROM sys_etl_data_source";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<EtlDataSource> queryEtlDataSources(int page, int pageSize) {
        String sql = "SELECT id, source_name, `db_schema`, db_type, db_ip, db_port, db_name, db_user_name, db_pwd, alive, create_time, status FROM sys_etl_data_source WHERE status = 1 LIMIT ?,?";
        return jdbcTemplate.query(sql, new EtlDataSourceMapper(), (page - 1) * pageSize, pageSize);
    }

    public EtlDataSource queryEtlDataSource(String id) {
        String sql = "SELECT id, source_name, db_type, db_ip, db_port, db_schema, db_name, db_user_name, db_pwd, alive, create_time, `status` FROM sys_etl_data_source WHERE id = ? AND status = 1";
        List<EtlDataSource> list = jdbcTemplate.query(sql, new EtlDataSourceMapper(), id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public boolean updateEtlDataSource(EtlDataSource etlDataSource) {
        String sql = "UPDATE sys_etl_data_source SET source_name = ?, db_type = ?, db_ip = ?, db_port = ?, db_name = ?, db_user_name = ?, db_pwd = ?, alive = ?, db_schema = ? WHERE id = ?";
        return jdbcTemplate.update(sql, etlDataSource.getSourceName(), etlDataSource.getDbType(), etlDataSource.getDbIp(), etlDataSource.getDbPort(), etlDataSource.getDbName(), etlDataSource.getDbUsername(), etlDataSource.getDbPwd(), etlDataSource.getAlive(), etlDataSource.getDbSchema(), etlDataSource.getId()) >= 0;
    }

    public EtlDataSource addEtlDataSource(EtlDataSource etlDataSource) {
        etlDataSource.setId(UUID.randomUUID().toString());
        String sql = "INSERT INTO sys_etl_data_source ( id, source_name, db_type, db_ip, db_port, db_name, db_user_name, db_pwd, alive, create_time, db_schema ) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        boolean flag = jdbcTemplate.update(sql, etlDataSource.getId(), etlDataSource.getSourceName(), etlDataSource.getDbType(), etlDataSource.getDbIp(), etlDataSource.getDbPort(), etlDataSource.getDbName(), etlDataSource.getDbUsername(), etlDataSource.getDbPwd(), etlDataSource.getAlive(), System.currentTimeMillis(), etlDataSource.getDbSchema()) > 0;
        if (flag) {
            return etlDataSource;
        }
        return null;
    }

    public boolean entityDel(String id) {
        String sql = "DELETE FROM sys_etl_entity WHERE ID = ?";
        return jdbcTemplate.update(sql, id) >= 0;
    }

    public boolean projectDel(String id) {
        String sql = "UPDATE sys_etl_project SET status = 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id) >= 0;
    }

    public boolean fieldDel(String id) {
        String sql = "DELETE FROM sys_etl_field WHERE id = ?";
        return jdbcTemplate.update(sql, id) >= 0;
    }

    public boolean chgJobStatus(String entityId, int status) {
        String sql = "UPDATE sys_etl_entity SET is_exec = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, entityId) >= 0;
    }

    public boolean chgApiJobStatus(String entityId, int status) {
        String sql = "UPDATE sys_etl_entity SET is_api_exec = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, entityId) >= 0;
    }

    public List<EtlAllTable> queryEtlAllTables(String dbId) {
        String sql = "SELECT id, ds_id, `name`, `comment`, create_time FROM sys_etl_all_table WHERE ds_id = ? ORDER BY `name`";
        return jdbcTemplate.query(sql, new EtlAllTableMapper(), dbId);
    }

    public List<EtlAllTable> queryEtlAllTables(String dbId, int page, int pageSize) {
        String sql = "SELECT id, ds_id, `name`, `comment`, create_time FROM sys_etl_all_table WHERE ds_id = ? ORDER BY `name` LIMIT ?,?";
        return jdbcTemplate.query(sql, new EtlAllTableMapper(), dbId, (page - 1) * pageSize, pageSize);
    }

    public EtlAllTable querySrcEtlAllTable(String prjId, String tabName) {
        String sql = "SELECT eat.id, eat.ds_id, eat.`name`, eat.`comment`, eat.create_time FROM sys_etl_all_table eat, sys_etl_project ep WHERE eat.ds_id = ep.SRC_DB_ID AND eat.`name` = ? AND ep.ID = ?";
        List<EtlAllTable> list = jdbcTemplate.query(sql, new EtlAllTableMapper(), tabName, prjId);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public EtlAllTable queryDesEtlAllTable(String prjId, String tabName) {
        String sql = "SELECT eat.id, eat.ds_id, eat.`name`, eat.`comment`, eat.create_time FROM sys_etl_all_table eat, sys_etl_project ep WHERE eat.ds_id = ep.DES_DB_ID AND eat.`name` = ? AND ep.ID = ?";
        List<EtlAllTable> list = jdbcTemplate.query(sql, new EtlAllTableMapper(), tabName, prjId);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<EtlAllField> queryPrimaryKey(String tableId) {
        String sql = "SELECT id, table_id, `name`, pri_key, `comment`, create_time FROM sys_etl_all_field WHERE table_id = ? AND pri_key = 1";
        return jdbcTemplate.query(sql, new EtlAllFieldMapper(), tableId);
    }

    public List<EtlAllField> queryFields(EtlAllTable table) {
        String sql = "SELECT id, table_id, `name`, pri_key, `comment`, create_time FROM sys_etl_all_field WHERE `name` != 'SYS__CREATE_OR_UPDATE_TIME' AND table_id = ? ORDER BY `name`";
        return jdbcTemplate.query(sql, new EtlAllFieldMapper(), table.getId());
    }

    public List<Map<String, Object>> queryEtlStartedEntities() {
        String sql = "SELECT id, etl_id FROM sys_etl_entity WHERE is_exec = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public List<EtlEntity> queryStartedApiEntities() {
        String sql = "SELECT id, prj_id, src_tab_id, src_tab_name, des_tab_id, des_tab_name, src_primary_key, des_primary_key, description, etl_id, create_time, `repeat`, schedule_type, interval_second, interval_minute, fixed_hour, fixed_minute, fixed_weekday, fixed_day, is_exec, is_api_exec, last_trans_time, `condition` FROM sys_etl_entity WHERE is_api_exec = 1";
        return jdbcTemplate.query(sql, new EtlEntityMapper());
    }

    public boolean stopAllEtlJob(String prjId) {
        String sql = "UPDATE sys_etl_entity SET is_exec = 0 WHERE prj_id = ?";
        return jdbcTemplate.update(sql, prjId) >= 0;
    }

    public void delTables(final List<String> tableIdList) {
        StringBuffer sb = new StringBuffer("('").append(tableIdList.get(0)).append("'");
        for (int i = 1; i < tableIdList.size(); i++) {
            sb.append(",'").append(tableIdList.get(i)).append("'");
        }
        sb.append(")");

        String[] sql = new String[2];
        sql[0] = "DELETE FROM `sys_etl_all_table` WHERE id in " + sb.toString();
        sql[1] = "DELETE FROM `sys_etl_all_field` WHERE table_id in " + sb.toString();
        jdbcTemplate.batchUpdate(sql);
    }

    public List<EtlAllField> queryEtlAllFields(String tableId) {
        String sql = "SELECT id, table_id, `name`, pri_key, `comment`, create_time FROM sys_etl_all_field WHERE table_id = ?";
        return jdbcTemplate.query(sql, new EtlAllFieldMapper(), tableId);
    }

    public void delFields(final List<String> fieldIdList) {
        String sql = "DELETE FROM `sys_etl_all_field` WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, fieldIdList.get(i));
            }

            @Override
            public int getBatchSize() {
                return fieldIdList.size();
            }
        });
    }

    public long queryEtlAllTableCount(String dsId) {
        String sql = "SELECT COUNT(*) FROM sys_etl_all_table WHERE ds_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, dsId);
    }

    public void updateApiTransTime(EtlEntity entity, String now) {
        String sql = "UPDATE sys_etl_entity SET last_trans_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, now, entity.getId());
    }

    public String queryLastTransTime(EtlEntity entity) {
        String sql = "SELECT last_trans_time FROM sys_etl_entity WHERE id = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, entity.getId());
        if (list.size() > 0) {
            return String.valueOf(list.get(0).get("last_trans_time"));
        }
        return X.TIME.INIT;
    }

    public boolean stopAllApiJob(String prjId) {
        String sql = "UPDATE sys_etl_entity SET is_api_exec = 0 WHERE prj_id = ?";
        return jdbcTemplate.update(sql, prjId) >= 0;
    }

    public DbSchema queryDbSchema(EtlProject project) {
        String sql = "SELECT seds_src.db_schema AS src_db_schema, seds_des.db_schema AS des_db_schema FROM sys_etl_project sep LEFT JOIN sys_etl_data_source seds_src ON seds_src.id = sep.src_db_id LEFT JOIN sys_etl_data_source seds_des ON seds_des.id = sep.des_db_id WHERE sep.id = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, project.getId());


        if (list.size() > 0) {
            DbSchema dbSchema = new DbSchema();
            Map<String, Object> map = list.get(0);
            dbSchema.setSrcDbSchema(String.valueOf(map.get("src_db_schema")));
            dbSchema.setDesDbSchema(String.valueOf(map.get("des_db_schema")));
            return dbSchema;
        }
        return null;
    }
}
