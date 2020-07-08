package net.ys.service;

import net.ys.bean.*;
import net.ys.constant.DbType;
import net.ys.dao.DbDao;
import net.ys.dao.EtlDao;
import net.ys.utils.DBUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * User: NMY
 * Date: 18-4-24
 */

@Service
public class DbService {

    @Resource
    private DbDao dbDao;

    @Resource
    private EtlDao etlDao;

    /**
     * 扫描数据源，获取表信息
     */
    @Scheduled(cron = "1 */20 0 * * ?")
    public void genTable() {
        //1、读表获取数据源
        List<EtlDataSource> dataSources = dbDao.queryDataSources();

        //2、获取表信息，入库
        List<DbTable> tables = null;
        int type;
        List<EtlAllTable> etlAllTables;
        List<String> tableIdList;
        for (EtlDataSource dataSource : dataSources) {
            type = dataSource.getDbType();

            if (type == DbType.MY_SQL.type) {
                tables = DBUtil.getTablesMySql(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            } else if (type == DbType.ORACLE.type) {
                tables = DBUtil.getTablesOracle(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            } else if (type == DbType.MS_SQL.type) {
                tables = DBUtil.getTablesMSSQL(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            }

            if (tables != null && tables.size() > 0) {
                dbDao.addTables(dataSource.getId(), tables);
            }

            etlAllTables = etlDao.queryEtlAllTables(dataSource.getId());
            tableIdList = compareTables(tables, etlAllTables);

            if (tableIdList.size() > 0) {
                etlDao.delTables(tableIdList);
            }
        }
    }

    private List<String> compareTables(List<DbTable> tables, List<EtlAllTable> sysTables) {
        List<String> list = new ArrayList<String>();

        if (sysTables.size() > 0) {
            boolean exist;
            for (EtlAllTable sysTable : sysTables) {
                exist = false;
                for (DbTable table : tables) {
                    if (table.getTableName().equalsIgnoreCase(sysTable.getName())) {
                        exist = true;
                        break;
                    }
                }

                if (!exist) {
                    list.add(sysTable.getId());
                }
            }
        }
        return list;
    }

    /**
     * 扫描表，获取字段信息
     */
    @Scheduled(cron = "50 */20 0 * * ?")
    public void genField() {

        List<EtlDataSource> dataSources = dbDao.queryDataSources();
        List<EtlAllTable> tables;
        List<DbField> fields;
        int type;
        List<EtlAllField> etlAllFields;
        List<String> fieldIdList;

        for (EtlDataSource dataSource : dataSources) {
            tables = dbDao.queryEtlAllTables(dataSource);
            type = dataSource.getDbType();
            if (type == DbType.MY_SQL.type) {
                for (EtlAllTable table : tables) {
                    fields = DBUtil.getFieldsMySql(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), table, dataSource.getDbUsername(), dataSource.getDbPwd());
                    if (fields != null && fields.size() > 0) {
                        dbDao.addFields(fields);
                    }

                    etlAllFields = etlDao.queryEtlAllFields(table.getId());
                    fieldIdList = compareFields(fields, etlAllFields);
                    if (fieldIdList.size() > 0) {
                        etlDao.delFields(fieldIdList);
                    }
                }
            } else if (type == DbType.ORACLE.type) {
                for (EtlAllTable table : tables) {
                    fields = DBUtil.getFieldsOracle(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), table, dataSource.getDbUsername(), dataSource.getDbPwd());
                    if (fields != null && fields.size() > 0) {
                        dbDao.addFields(fields);
                    }

                    etlAllFields = etlDao.queryEtlAllFields(table.getId());
                    fieldIdList = compareFields(fields, etlAllFields);
                    if (fieldIdList.size() > 0) {
                        etlDao.delFields(fieldIdList);
                    }
                }
            } else if (type == DbType.MS_SQL.type) {
                for (EtlAllTable table : tables) {
                    fields = DBUtil.getFieldsMSSQL(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), table, dataSource.getDbUsername(), dataSource.getDbPwd());
                    if (fields != null && fields.size() > 0) {
                        dbDao.addFields(fields);
                    }

                    etlAllFields = etlDao.queryEtlAllFields(table.getId());
                    fieldIdList = compareFields(fields, etlAllFields);
                    if (fieldIdList.size() > 0) {
                        etlDao.delFields(fieldIdList);
                    }
                }
            }
        }
    }

    private List<String> compareFields(List<DbField> fields, List<EtlAllField> etlAllFields) {
        List<String> list = new ArrayList<String>();

        if (etlAllFields.size() > 0) {
            boolean exist;
            for (EtlAllField etlAllField : etlAllFields) {
                exist = false;
                for (DbField field : fields) {
                    if (field.getFieldName().equalsIgnoreCase(etlAllField.getName())) {
                        exist = true;
                        break;
                    }
                }

                if (!exist) {
                    list.add(etlAllField.getId());
                }
            }
        }
        return list;
    }
}