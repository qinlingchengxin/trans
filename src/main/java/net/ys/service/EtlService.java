package net.ys.service;

import net.ys.bean.*;
import net.ys.constant.DbType;
import net.ys.constant.X;
import net.ys.dao.DbDao;
import net.ys.dao.EtlDao;
import net.ys.utils.DBUtil;
import net.ys.utils.KettleUtil;
import net.ys.utils.LogUtil;
import net.ys.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EtlService {

    @Resource
    private EtlDao etlDao;

    @Resource
    private DbDao dbDao;

    public static Map<String, TimerTask> jobs;
    public static Timer timer;

    //单次传输最大限制
    static final int PAGE_SIZE = 50;
    static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";

    static {
        jobs = new ConcurrentHashMap<String, TimerTask>(100);
        timer = new Timer();
    }

    public long queryEtlEntityCount(String prjId) {
        return etlDao.queryEtlEntityCount(prjId);
    }

    public List<EtlEntity> queryEtlEntities(String prjId, int page, int pageSize) {
        return etlDao.queryEtlEntities(prjId, page, pageSize);
    }

    public EtlEntity queryEtlEntity(String id) {
        return etlDao.queryEtlEntity(id);
    }

    public boolean updateEtlEntity(EtlEntity etlEntity) {
        return etlDao.updateEtlEntity(etlEntity);
    }

    public EtlEntity addEtlEntity(EtlEntity etlEntity) {
        return etlDao.addEtlEntity(etlEntity);
    }

    public long queryEtlFieldCount(String entityId) {
        return etlDao.queryEtlFieldCount(entityId);
    }

    public List<EtlField> queryEtlFields(String entityId, int page, int pageSize) {
        return etlDao.queryEtlFields(entityId, page, pageSize);
    }

    /**
     * 生成映射文件的字段
     *
     * @param entityId
     * @return
     */
    public List<EtlField> queryEtlKtrFields(String entityId) {
        return etlDao.queryEtlKtrFields(entityId);
    }

    public EtlField addEtlField(EtlField etlField) {
        return etlDao.addEtlField(etlField);
    }

    public EtlProject queryEtlKtrProject(String entityId) {
        return etlDao.queryEtlKtrProject(entityId);
    }

    public long queryEtlProjectCount() {
        return etlDao.queryEtlProjectCount();
    }

    public List<EtlProject> queryEtlProjects(int page, int pageSize) {
        return etlDao.queryEtlProjects(page, pageSize);
    }

    public EtlProject queryEtlProject(String id) {
        return etlDao.queryEtlProject(id);
    }

    public boolean updateEtlProject(EtlProject etlProject) {
        return etlDao.updateEtlProject(etlProject);
    }

    public EtlProject addEtlProject(EtlProject etlProject) {
        return etlDao.addEtlProject(etlProject);
    }

    public List<EtlAllField> queryFields(EtlAllTable table) {
        return etlDao.queryFields(table);
    }

    public long queryEtlDataSourceCount() {
        return etlDao.queryEtlDataSourceCount();
    }

    public List<EtlDataSource> queryEtlDataSources(int page, int pageSize) {
        return etlDao.queryEtlDataSources(page, pageSize);
    }

    public EtlDataSource queryEtlDataSource(String id) {
        return etlDao.queryEtlDataSource(id);
    }

    public boolean updateEtlDataSource(EtlDataSource etlDataSource) {
        boolean flag = testConnDs(etlDataSource);
        etlDataSource.setAlive(flag ? 1 : 0);
        return etlDao.updateEtlDataSource(etlDataSource);
    }

    public EtlDataSource addEtlDataSource(EtlDataSource etlDataSource) {
        boolean flag = testConnDs(etlDataSource);
        etlDataSource.setAlive(flag ? 1 : 0);
        return etlDao.addEtlDataSource(etlDataSource);
    }

    public List<EtlAllTable> querySrcTables(EtlProject project) {
        return etlDao.queryEtlAllTables(project.getSrcDbId());
    }

    public List<EtlAllTable> queryDesTables(EtlProject project) {
        return etlDao.queryEtlAllTables(project.getDesDbId());
    }

    public List<EtlAllField> queryPrimaryKey(String tableId) {
        return etlDao.queryPrimaryKey(tableId);
    }

    public boolean testConnDs(EtlDataSource etlDataSource) {
        int dbType = etlDataSource.getDbType();
        boolean flag = false;
        if (dbType == DbType.MY_SQL.type) {
            flag = DBUtil.testConnMySql(etlDataSource);
        } else if (dbType == DbType.ORACLE.type) {
            flag = DBUtil.testConnOracle(etlDataSource);
        } else if (dbType == DbType.MS_SQL.type) {
            flag = DBUtil.testConnMSSQL(etlDataSource);
        } else if (dbType == DbType.KING_BASE.type) {
            flag = DBUtil.testConnKingBase(etlDataSource);
        }
        return flag;
    }

    public boolean entityDel(String id) {
        return etlDao.entityDel(id);
    }

    public boolean projectDel(String id) {
        return etlDao.projectDel(id);
    }

    public boolean fieldDel(String id) {
        return etlDao.fieldDel(id);
    }

    public boolean chgJobStatus(String entityId, int status) {
        return etlDao.chgJobStatus(entityId, status);
    }

    public boolean chgApiJobStatus(String entityId, int status) {
        return etlDao.chgApiJobStatus(entityId, status);
    }

    public EtlAllTable querySrcEtlAllTable(String prjId, String tabName) {
        return etlDao.querySrcEtlAllTable(prjId, tabName);
    }

    public EtlAllTable queryDesEtlAllTable(String prjId, String tabName) {
        return etlDao.queryDesEtlAllTable(prjId, tabName);
    }

    /**
     * 启动所有已开启的etl进行传输
     */
    public List<Map<String, Object>> queryStartedEntities() {
        return etlDao.queryEtlStartedEntities();
    }

    /**
     * 启动所有已开启的api任务进行传输
     */
    public List<EtlEntity> queryStartedApiEntities() {
        return etlDao.queryStartedApiEntities();
    }

    public boolean genTables(EtlDataSource dataSource) {
        boolean flag = false;
        if (dataSource != null && dataSource.getAlive() == 1) {
            int type = dataSource.getDbType();

            List<DbTable> tables = null;
            if (type == DbType.MY_SQL.type) {
                tables = DBUtil.getTablesMySql(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            } else if (type == DbType.ORACLE.type) {
                tables = DBUtil.getTablesOracle(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            } else if (type == DbType.MS_SQL.type) {
                tables = DBUtil.getTablesMSSQL(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            } else if (type == DbType.KING_BASE.type) {
                tables = DBUtil.getTablesKingBase(dataSource);
            }

            if (tables != null && tables.size() > 0) {
                dbDao.addTables(dataSource.getId(), tables);
            }

            List<EtlAllTable> etlAllTables = etlDao.queryEtlAllTables(dataSource.getId());
            List<String> tableIdList = compareTables(tables, etlAllTables);

            if (tableIdList.size() > 0) {
                etlDao.delTables(tableIdList);
            }

            flag = true;
        }
        return flag;
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

    public boolean genFields(EtlDataSource dataSource) {
        boolean flag = false;
        List<EtlAllTable> tables = dbDao.queryEtlAllTables(dataSource);
        int type = dataSource.getDbType();
        List<DbField> fields;
        List<EtlAllField> etlAllFields;
        List<String> fieldIdList;
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
            flag = true;
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
            flag = true;
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
            flag = true;
        } else if (type == DbType.KING_BASE.type) {
            for (EtlAllTable table : tables) {
                fields = DBUtil.getFieldsKingBase(dataSource, table);
                if (fields != null && fields.size() > 0) {
                    dbDao.addFields(fields);
                }

                etlAllFields = etlDao.queryEtlAllFields(table.getId());
                fieldIdList = compareFields(fields, etlAllFields);
                if (fieldIdList.size() > 0) {
                    etlDao.delFields(fieldIdList);
                }
            }
            flag = true;
        }

        return flag;
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

    public void genTablesAndFields(final EtlDataSource etlDataSource) {
        boolean flag = genTables(etlDataSource);
        if (flag) {
            genFields(etlDataSource);
        }
    }

    public boolean stopAllEtlJob(String prjId) {
        return etlDao.stopAllEtlJob(prjId);
    }

    public boolean stopAllApiJob(String prjId) {
        return etlDao.stopAllApiJob(prjId);
    }

    public long queryEtlAllTableCount(String dsId) {
        return etlDao.queryEtlAllTableCount(dsId);
    }

    public List<EtlAllTable> queryEtlAllTables(String dsId, int page, int pageSize) {
        return etlDao.queryEtlAllTables(dsId, page, pageSize);
    }

    /**
     * 启动api传输任务
     *
     * @param project
     * @param entity
     * @param fields
     * @param dbSchema
     * @return
     */
    public boolean startApiJob(EtlProject project, EtlEntity entity, List<EtlField> fields, DbSchema dbSchema) {

        try {
            Map<String, String> sql = genSql(project.getCenterDbType(), entity, fields, dbSchema);

            TimerTask timerTask = new ApiTimerTask(project, entity, sql.get("sqlSelect"), sql.get("sqlCount"), fields, dbSchema);
            int repeat = entity.getRepeat();
            if (repeat == 0) {//无需重复
                timer.schedule(timerTask, 3000);
                return true;
            }

            int intervalMinute = entity.getIntervalMinute();
            int intervalSecond = entity.getIntervalSecond();
            long delay = X.TIME.MINUTE_MILLISECOND * intervalMinute + X.TIME.SECOND_MILLISECOND * intervalSecond;
            timer.schedule(timerTask, 3000, delay);
            jobs.put(entity.getId(), timerTask);//加入到内存中，以便后续停止操作
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        }

        return false;
    }

    /**
     * 重启etl传输任务，重启服务用
     *
     * @return
     */
    public void restartEtlJob() {
        try {
            List<Map<String, Object>> etlEntities = queryStartedEntities();
            if (etlEntities.size() > 0) {
                for (Map<String, Object> etlEntity : etlEntities) {
                    String entityId = String.valueOf(etlEntity.get("id"));
                    String etlId = String.valueOf(etlEntity.get("etl_id"));
                    KettleUtil.startEtlJob(etlId, entityId);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 重启api传输任务，重启服务用
     *
     * @return
     */
    public void restartApiJob() {
        try {
            List<EtlEntity> etlEntities = queryStartedApiEntities();
            List<EtlField> fields;
            EtlEntity entity;
            EtlProject project;
            Map<String, String> sql;
            TimerTask timerTask;
            for (EtlEntity etlEntity : etlEntities) {
                String entityId = etlEntity.getId();
                fields = queryEtlKtrFields(entityId);
                entity = queryEtlEntity(entityId);
                project = queryEtlKtrProject(entityId);
                DbSchema dbSchema = queryDbSchema(project);
                sql = genSql(project.getCenterDbType(), entity, fields, dbSchema);
                timerTask = new ApiTimerTask(project, entity, sql.get("sqlSelect"), sql.get("sqlCount"), fields, dbSchema);
                int intervalMinute = entity.getIntervalMinute();
                int intervalSecond = entity.getIntervalSecond();
                long delay = X.TIME.MINUTE_MILLISECOND * intervalMinute + X.TIME.SECOND_MILLISECOND * intervalSecond;
                timer.schedule(timerTask, 3000, delay);
                jobs.put(entity.getId(), timerTask);//加入到内存中，以便后续停止操作
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    public boolean stopApiJob(String entityId) {
        try {
            TimerTask timerTask = jobs.get(entityId);
            if (timerTask == null) {
                return true;
            }
            timerTask.cancel();
            jobs.remove(entityId);
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return false;
    }

    private Map<String, String> genSql(int dbType, EtlEntity entity, List<EtlField> fields, DbSchema dbSchema) {
        Map<String, String> sql = new HashMap<String, String>();

        String quotLeft;
        String quotRight;
        StringBuffer sqlSelect;
        StringBuffer sqlCount;
        if (dbType == DbType.MY_SQL.type) {
            quotLeft = "`";
            quotRight = "`";

            sqlSelect = new StringBuffer("SELECT ").append(quotLeft).append(fields.get(0).getSrcFieldName()).append(quotRight);
            for (int i = 1; i < fields.size(); i++) {
                sqlSelect.append(", ").append(quotLeft).append(fields.get(i).getSrcFieldName()).append(quotRight);
            }
            sqlSelect.append(", ").append(quotLeft).append("SYS__CREATE_OR_UPDATE_TIME").append(quotRight);
            sqlSelect.append(" FROM ")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE SYS__CREATE_OR_UPDATE_TIME > ? AND SYS__CREATE_OR_UPDATE_TIME <= ? ORDER BY SYS__CREATE_OR_UPDATE_TIME, ").append(quotLeft).append(entity.getSrcPrimaryKey()).append(quotRight).append(" LIMIT ?,?");

            sqlCount = new StringBuffer("SELECT COUNT(*) AS C FROM ")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE SYS__CREATE_OR_UPDATE_TIME > ? AND SYS__CREATE_OR_UPDATE_TIME <= ?");

        } else if (dbType == DbType.ORACLE.type) {
            quotLeft = "\"";
            quotRight = "\"";

            sqlSelect = new StringBuffer("SELECT * FROM ( SELECT T1.*, ROWNUM AS ORACLE___RW FROM (SELECT ").append(quotLeft).append(fields.get(0).getSrcFieldName()).append(quotRight);
            for (int i = 1; i < fields.size(); i++) {
                sqlSelect.append(", ").append(quotLeft).append(fields.get(i).getSrcFieldName()).append(quotRight);
            }
            sqlSelect.append(" FROM ")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE to_char(SYS__CREATE_OR_UPDATE_TIME,'yyyy-mm-dd HH24:mi:ss') > ? AND to_char(SYS__CREATE_OR_UPDATE_TIME,'yyyy-mm-dd HH24:mi:ss') <= ? ORDER BY SYS__CREATE_OR_UPDATE_TIME, ").append(quotLeft).append(entity.getSrcPrimaryKey()).append(quotRight).append(") T1) T2 WHERE T2.ORACLE___RW >= ? AND T2.ORACLE___RW < ? ");

            sqlCount = new StringBuffer("SELECT COUNT(*) AS C FROM ")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE to_char(SYS__CREATE_OR_UPDATE_TIME,'yyyy-mm-dd HH24:mi:ss') > ? AND to_char(SYS__CREATE_OR_UPDATE_TIME,'yyyy-mm-dd HH24:mi:ss') <= ?");

        } else if (dbType == DbType.KING_BASE.type) {
            quotLeft = "\"";
            quotRight = "\"";

            sqlSelect = new StringBuffer("SELECT ").append(quotLeft).append(fields.get(0).getSrcFieldName()).append(quotRight);
            for (int i = 1; i < fields.size(); i++) {
                sqlSelect.append(", ").append(quotLeft).append(fields.get(i).getSrcFieldName()).append(quotRight);
            }

            sqlSelect.append(", ").append(quotLeft).append("SYS__CREATE_OR_UPDATE_TIME").append(quotRight);
            sqlSelect.append(" FROM ")
                    .append(quotLeft).append(dbSchema.getSrcDbSchema()).append(quotRight).append(".")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE SYS__CREATE_OR_UPDATE_TIME > ? AND SYS__CREATE_OR_UPDATE_TIME <= ? ORDER BY SYS__CREATE_OR_UPDATE_TIME, ").append(quotLeft).append(entity.getSrcPrimaryKey()).append(quotRight).append(" LIMIT ?,?");

            sqlCount = new StringBuffer("SELECT COUNT(*) AS C FROM ")
                    .append(quotLeft).append(dbSchema.getSrcDbSchema()).append(quotRight).append(".")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE SYS__CREATE_OR_UPDATE_TIME > ? AND SYS__CREATE_OR_UPDATE_TIME <= ?");
        } else {
            quotLeft = "[";
            quotRight = "]";

            sqlSelect = new StringBuffer("SELECT ").append(quotLeft).append(fields.get(0).getSrcFieldName()).append(quotRight).append(" ");
            for (int i = 1; i < fields.size(); i++) {
                sqlSelect.append(", ").append(quotLeft).append(fields.get(i).getSrcFieldName()).append(quotRight).append(" ");
            }
            sqlSelect.append(" FROM ")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE SYS__CREATE_OR_UPDATE_TIME > ? AND SYS__CREATE_OR_UPDATE_TIME <= ? ORDER BY SYS__CREATE_OR_UPDATE_TIME, ").append(quotLeft).append(entity.getSrcPrimaryKey()).append(quotRight).append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

            sqlCount = new StringBuffer("SELECT COUNT(*) AS C FROM ")
                    .append(quotLeft).append(entity.getSrcTabName()).append(quotRight)
                    .append(" WHERE SYS__CREATE_OR_UPDATE_TIME > ? AND SYS__CREATE_OR_UPDATE_TIME <= ?");
        }

        sql.put("sqlSelect", sqlSelect.toString());
        sql.put("sqlCount", sqlCount.toString());
        return sql;
    }

    /**
     * 生成问号字符串
     *
     * @param size
     * @return
     */
    public String genMark(int size) {
        StringBuffer sb = new StringBuffer("?");
        for (int i = 1; i < size; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

    public DbSchema queryDbSchema(EtlProject project) {
        return etlDao.queryDbSchema(project);
    }

    class ApiTimerTask extends TimerTask {

        private EtlProject project;
        private EtlEntity entity;
        private String sqlSelect;
        private String sqlCount;
        private List<EtlField> fields;
        private DbSchema dbSchema;

        ApiTimerTask(EtlProject project, EtlEntity entity, String sqlSelect, String sqlCount, List<EtlField> fields, DbSchema dbSchema) {
            this.project = project;
            this.entity = entity;
            this.sqlSelect = sqlSelect;
            this.sqlCount = sqlCount;
            this.fields = fields;
            this.dbSchema = dbSchema;
        }

        @Override
        public void run() {
            Connection connectionSrc = null;
            Connection connectionDes = null;
            try {
                int srcDbType = project.getCenterDbType();
                int desDbType = project.getBusDbType();
                String now = TimeUtil.genYmdHms();
                String lastTransTime = etlDao.queryLastTransTime(entity);
                String desTableName = entity.getDesTabName();

                if (srcDbType == DbType.MY_SQL.type) {
                    connectionSrc = DBUtil.getConnectionMySql(project.getCenterDbIp(), Integer.parseInt(project.getCenterDbPort()), project.getCenterDbName(), project.getCenterDbUsername(), project.getCenterDbPwd());
                } else if (srcDbType == DbType.ORACLE.type) {
                    connectionSrc = DBUtil.getConnectionOracle(project.getCenterDbIp(), Integer.parseInt(project.getCenterDbPort()), project.getCenterDbName(), project.getCenterDbUsername(), project.getCenterDbPwd());
                } else if (srcDbType == DbType.KING_BASE.type) {
                    connectionSrc = DBUtil.getConnectionKingBase(project.getCenterDbIp(), Integer.parseInt(project.getCenterDbPort()), project.getCenterDbName(), project.getCenterDbUsername(), project.getCenterDbPwd());
                } else {
                    connectionSrc = DBUtil.getConnectionMSSQL(project.getCenterDbIp(), Integer.parseInt(project.getCenterDbPort()), project.getCenterDbName(), project.getCenterDbUsername(), project.getCenterDbPwd());
                }

                if (desDbType == DbType.MY_SQL.type) {
                    connectionDes = DBUtil.getConnectionMySql(project.getBusDbIp(), Integer.parseInt(project.getBusDbPort()), project.getBusDbName(), project.getBusDbUsername(), project.getBusDbPwd());
                } else if (desDbType == DbType.ORACLE.type) {
                    connectionDes = DBUtil.getConnectionOracle(project.getBusDbIp(), Integer.parseInt(project.getBusDbPort()), project.getBusDbName(), project.getBusDbUsername(), project.getBusDbPwd());
                } else if (desDbType == DbType.KING_BASE.type) {
                    connectionDes = DBUtil.getConnectionKingBase(project.getBusDbIp(), Integer.parseInt(project.getBusDbPort()), project.getBusDbName(), project.getBusDbUsername(), project.getBusDbPwd());
                } else {
                    connectionDes = DBUtil.getConnectionMSSQL(project.getBusDbIp(), Integer.parseInt(project.getBusDbPort()), project.getBusDbName(), project.getBusDbUsername(), project.getBusDbPwd());
                }

                long dataCount = DBUtil.getDataCount(connectionSrc, sqlCount, lastTransTime, now);
                LogUtil.debug("tableName:::" + desTableName + "\t\tlastTransTime:::" + lastTransTime + "\t\tdataCount:::" + dataCount + "\t\tuploadData-start");
                if (dataCount > 0) {
                    long page = dataCount / PAGE_SIZE + (dataCount % PAGE_SIZE == 0 ? 0 : 1);
                    List<Map<String, Object>> data;
                    boolean flag;
                    int startPos;
                    int endPos;
                    for (int i = 1; i <= page; i++) {
                        startPos = (i - 1) * PAGE_SIZE;
                        if (srcDbType == 0 || srcDbType == 2) {
                            endPos = PAGE_SIZE;
                        } else {//oracle特殊
                            endPos = i * PAGE_SIZE;
                        }

                        data = DBUtil.getData(connectionSrc, sqlSelect, lastTransTime, now, startPos, endPos);
                        flag = addDataStep(connectionDes, desTableName, data, fields, desDbType, entity, dbSchema);//直接插入到目标数据库中
                        if (!flag) {
                            return;
                        }
                    }
                }

                etlDao.updateApiTransTime(entity, now);

                LogUtil.debug("tableName:::" + desTableName + "\t\tuploadData-end");
            } catch (Exception e) {
                LogUtil.error(e);
            } finally {
                if (connectionSrc != null) {
                    DBUtil.closeConnection(connectionSrc);
                }
                if (connectionDes != null) {
                    DBUtil.closeConnection(connectionDes);
                }
            }
        }
    }

    private boolean addDataStep(Connection connectionDes, String tableName, List<Map<String, Object>> data, List<EtlField> fields, int dbType, EtlEntity entity, DbSchema dbSchema) {

        String quotLeft;
        String quotRight;
        if (dbType == DbType.MY_SQL.type) {
            quotLeft = "`";
            quotRight = "`";
        } else if (dbType == DbType.ORACLE.type || dbType == DbType.KING_BASE.type) {
            quotLeft = "\"";
            quotRight = "\"";
        } else {
            quotLeft = "[";
            quotRight = "]";
        }

        Map<String, Object> map = data.get(0);
        Set<String> keys = map.keySet();
        StringBuffer sql = new StringBuffer("INSERT INTO ");
        if (dbType == DbType.KING_BASE.type) {
            sql.append(quotLeft).append(dbSchema.getDesDbSchema()).append(quotRight).append(".");
        }

        sql.append(quotLeft).append(tableName).append(quotRight);
        if (dbType == DbType.KING_BASE.type) {
            sql.append(" AS d");
        }

        sql.append(" (");

        boolean flag = false;

        for (EtlField field : fields) {
            if ("ORACLE___RW".equals(field.getDesFieldName())) {
                flag = true;
                continue;
            }
            sql.append(" ").append(quotLeft).append(field.getDesFieldName()).append(quotRight).append(",");
        }

        sql.append(" ").append(quotLeft).append("SYS__CREATE_OR_UPDATE_TIME").append(quotRight);

        int markSize;
        if (flag) {
            markSize = keys.size() - 1;
        } else {
            markSize = keys.size();
        }

        String sqlStr;
        if (dbType == DbType.MY_SQL.type) {
            sql.append(") ");
            //1、双向传输、请勿删除，解决类似ABA问题
            //sql.append("SELECT ").append(genMark(markSize)).append(" FROM ").append(quotLeft).append(tableName).append(quotRight).append(" WHERE ").append(quotLeft).append(entity.getDesPrimaryKey()).append(quotRight).append(" = ? AND `SYS__CREATE_OR_UPDATE_TIME` < ? ");
            //2、普通单向传输
            sql.append("VALUES (").append(genMark(markSize)).append(")");

            sql.append(" ON DUPLICATE KEY UPDATE ");
            for (String key : keys) {
                if ("ORACLE___RW".equals(key)) {
                    continue;
                }
                sql.append("`").append(key).append("` = ?,");
            }

            sql.deleteCharAt(sql.length() - 1);
            sqlStr = sql.toString();
        } else if (dbType == DbType.ORACLE.type) {
            sql.append(") VALUES (").append(genMark(markSize)).append(")");
            sqlStr = sql.toString();
        } else if (dbType == DbType.KING_BASE.type) {
            sql.append(") VALUES (").append(genMark(markSize)).append(")");

            sql.append(" ON CONFLICT (").append(quotLeft).append(entity.getDesPrimaryKey()).append(quotRight).append(") DO UPDATE SET ");
            for (String key : keys) {
                if ("ORACLE___RW".equals(key)) {
                    continue;
                }
                sql.append("\"").append(key).append("\" = ?,");
            }

            sql.deleteCharAt(sql.length() - 1);

            sql.append(" WHERE d.\"SYS__CREATE_OR_UPDATE_TIME\" < ? ");
            sqlStr = sql.toString();
        } else {
            sql.append(") VALUES (").append(genMark(markSize)).append(")");
            sqlStr = sql.toString();
        }

        LogUtil.debug(sqlStr);

        if (dbType == DbType.MY_SQL.type) {
            return DBUtil.addDataStepMysql(connectionDes, sqlStr, data, entity);
        } else if (dbType == DbType.ORACLE.type) {
            return DBUtil.addDataStepOracle(connectionDes, sqlStr, data);
        } else if (dbType == DbType.KING_BASE.type) {
            return DBUtil.addDataStepKingBase(connectionDes, sqlStr, data, entity);
        } else {//不会覆盖，待完善
            return DBUtil.addDataStepOracle(connectionDes, sqlStr, data);
        }
    }
}
