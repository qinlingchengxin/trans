package net.ys.mapper;

import net.ys.bean.EtlEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlEntityMapper implements RowMapper<EtlEntity> {
    @Override
    public EtlEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        EtlEntity etlEntity = new EtlEntity();
        etlEntity.setId(resultSet.getString("id"));
        etlEntity.setPrjId(resultSet.getString("prj_id"));
        etlEntity.setSrcTabId(resultSet.getString("src_tab_id"));
        etlEntity.setSrcTabName(resultSet.getString("src_tab_name"));
        etlEntity.setDesTabId(resultSet.getString("des_tab_id"));
        etlEntity.setDesTabName(resultSet.getString("des_tab_name"));
        etlEntity.setSrcPrimaryKey(resultSet.getString("src_primary_key"));
        etlEntity.setDesPrimaryKey(resultSet.getString("des_primary_key"));
        etlEntity.setDescription(resultSet.getString("description"));
        etlEntity.setEtlId(resultSet.getString("etl_id"));
        etlEntity.setCreateTime(resultSet.getLong("create_time"));
        etlEntity.setRepeat(resultSet.getInt("repeat"));
        etlEntity.setScheduleType(resultSet.getInt("schedule_type"));
        etlEntity.setIntervalSecond(resultSet.getInt("interval_second"));
        etlEntity.setIntervalMinute(resultSet.getInt("interval_minute"));
        etlEntity.setFixedHour(resultSet.getInt("fixed_hour"));
        etlEntity.setFixedMinute(resultSet.getInt("fixed_minute"));
        etlEntity.setFixedWeekday(resultSet.getInt("fixed_weekday"));
        etlEntity.setFixedDay(resultSet.getInt("fixed_day"));
        etlEntity.setExec(resultSet.getInt("is_exec"));
        etlEntity.setApiExec(resultSet.getInt("is_api_exec"));
        etlEntity.setLastTransTime(resultSet.getString("last_trans_time"));
        etlEntity.setCondition(resultSet.getString("condition"));
        return etlEntity;
    }
}