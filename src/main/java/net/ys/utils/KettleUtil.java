package net.ys.utils;

import net.ys.bean.DbSchema;
import net.ys.bean.EtlEntity;
import net.ys.bean.EtlField;
import net.ys.bean.EtlProject;
import net.ys.component.SysConfig;
import net.ys.constant.DbType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entries.writetofile.JobEntryWriteToFile;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

import java.io.File;
import java.util.*;

/**
 * kettle工具
 * User: NMY
 * Date: 18-3-13
 */
public class KettleUtil {

    static Map<String, Job> jobMap = new HashMap<String, Job>();

    /**
     * 生成ktr文件
     */
    public static boolean genKtrFile(EtlEntity entity, EtlProject project, List<EtlField> fields, String transName, DbSchema dbSchema) {
        try {
            KettleEnvironment.init();

            String quotesLeft;
            String quotesRight;
            int cdtI = project.getCenterDbType();
            TransMeta transMeta = new TransMeta();
            String cdt = project.getCenterDbType() == 0 ? "MySql" : project.getCenterDbType() == 1 ? "Oracle" : project.getCenterDbType() == 3 ? "KINGBASEES" : "MSSQL";
            DatabaseMeta srcDbMeta = new DatabaseMeta("src_db", cdt, "Native", project.getCenterDbIp(), project.getCenterDbName(), project.getCenterDbPort(), project.getCenterDbUsername(), project.getCenterDbPwd());
            Properties properties = srcDbMeta.getAttributes();

            if (DbType.MY_SQL.type == cdtI) {
                quotesLeft = "`";
                quotesRight = "`";
                properties.setProperty("EXTRA_OPTION_MYSQL.characterEncoding", "utf8");
            } else if (DbType.ORACLE.type == cdtI || DbType.KING_BASE.type == cdtI) {
                quotesLeft = "\"";
                quotesRight = "\"";
                properties.setProperty("EXTRA_OPTION_ORACLE.characterEncoding", "utf8");
            } else {
                quotesLeft = "[";
                quotesRight = "]";
                properties.setProperty("EXTRA_OPTION_MSSQLNATIVE.characterEncoding", "utf8");
            }

            srcDbMeta.setAttributes(properties);
            transMeta.addDatabase(srcDbMeta);

            int bdtI = project.getBusDbType();
            String bdt = project.getBusDbType() == 0 ? "MySql" : project.getBusDbType() == 1 ? "Oracle" : project.getBusDbType() == 3 ? "KINGBASEES" : "MSSQL";
            DatabaseMeta desDbMeta = new DatabaseMeta("des_db", bdt, "Native", project.getBusDbIp(), project.getBusDbName(), project.getBusDbPort(), project.getBusDbUsername(), project.getBusDbPwd());
            properties = desDbMeta.getAttributes();
            if (DbType.MY_SQL.type == bdtI) {
                properties.setProperty("EXTRA_OPTION_MYSQL.characterEncoding", "utf8");
            } else if (DbType.ORACLE.type == bdtI || DbType.KING_BASE.type == bdtI) {
                properties.setProperty("EXTRA_OPTION_ORACLE.characterEncoding", "utf8");
            } else {
                properties.setProperty("EXTRA_OPTION_MSSQLNATIVE.characterEncoding", "utf8");
            }
            desDbMeta.setAttributes(properties);
            transMeta.addDatabase(desDbMeta);

            //设置转化的名称
            transMeta.setName(transName);

            //registry是给每个步骤生成一个标识Id用
            PluginRegistry registry = PluginRegistry.getInstance();

            //----------------------------------------------------------------------
            //第一个表输入步骤(TableInputMeta)
            TableInputMeta tableInput = new TableInputMeta();
            String tableInputPluginId = registry.getPluginId(StepPluginType.class, tableInput);
            DatabaseMeta srcDb = transMeta.findDatabase("src_db");
            tableInput.setDatabaseMeta(srcDb);

            StringBuffer selectSql = new StringBuffer("SELECT ");
            selectSql.append(quotesLeft).append(fields.get(0).getSrcFieldName()).append(quotesRight);

            for (int i = 1, j = fields.size(); i < j; i++) {
                selectSql.append(", ").append(quotesLeft).append(fields.get(i).getSrcFieldName()).append(quotesRight);
            }

            selectSql.append(" FROM ");
            if (project.getCenterDbType() == DbType.KING_BASE.type) {
                selectSql.append(quotesLeft).append(dbSchema.getSrcDbSchema()).append(quotesRight).append(".");
            }

            selectSql.append(quotesLeft).append(entity.getSrcTabName()).append(quotesRight).append(" WHERE 1=1 ");

            String condition = entity.getCondition();
            if (StringUtils.isNotBlank(condition)) {
                selectSql.append(" AND ").append(condition);
            }

            tableInput.setSQL(selectSql.toString());

            StepMeta tableInputMetaStep = new StepMeta(tableInputPluginId, "table input", tableInput);
            tableInputMetaStep.setDraw(true);
            tableInputMetaStep.setLocation(100, 100);
            transMeta.addStep(tableInputMetaStep);

            //----------------------------------------------------------------------
            //第二个步骤插入与更新
            InsertUpdateMeta insertUpdateMeta = new InsertUpdateMeta();
            String insertUpdateMetaPluginId = registry.getPluginId(StepPluginType.class, insertUpdateMeta);
            DatabaseMeta desDb = transMeta.findDatabase("des_db");
            insertUpdateMeta.setDatabaseMeta(desDb);
            if (project.getBusDbType() == DbType.KING_BASE.type) {
                insertUpdateMeta.setSchemaName(dbSchema.getSrcDbSchema());
            }

            insertUpdateMeta.setTableName(entity.getDesTabName());
            insertUpdateMeta.setCommitSize("10000");

            //设置用来查询的关键字
            insertUpdateMeta.setKeyStream(new String[]{entity.getSrcPrimaryKey()});//流字段（src表的字段）
            insertUpdateMeta.setKeyLookup(new String[]{entity.getDesPrimaryKey()});//目标字段（des表的字段）
            insertUpdateMeta.setKeyStream2(new String[]{""});//一定要加上
            insertUpdateMeta.setKeyCondition(new String[]{"="});

            //设置要更新的字段
            List<String> srcFieldList = new ArrayList<String>();
            for (EtlField field : fields) {
                srcFieldList.add(field.getSrcFieldName());
            }

            String[] srcFieldArr = new String[srcFieldList.size()];
            srcFieldList.toArray(srcFieldArr);

            List<String> desFieldList = new ArrayList<String>();
            for (EtlField field : fields) {
                desFieldList.add(field.getDesFieldName());
            }

            String[] desFieldArr = new String[desFieldList.size()];
            desFieldList.toArray(desFieldArr);

            Boolean[] updateOrNot = new Boolean[srcFieldList.size()];// {false, true, true, true, true, true, true};

            for (int i = 0; i < updateOrNot.length; i++) {
                updateOrNot[i] = true;
            }

            insertUpdateMeta.setUpdateLookup(desFieldArr);
            insertUpdateMeta.setUpdateStream(srcFieldArr);
            insertUpdateMeta.setUpdate(updateOrNot);
            //添加步骤到转换中
            StepMeta insertUpdateStep = new StepMeta(insertUpdateMetaPluginId, "table output", insertUpdateMeta);
            insertUpdateStep.setDraw(true);
            insertUpdateStep.setLocation(250, 100);
            transMeta.addStep(insertUpdateStep);

            //添加hop把两个步骤关联起来
            transMeta.addTransHop(new TransHopMeta(tableInputMetaStep, insertUpdateStep));

            String transXml = transMeta.getXML();
            String transFilePath = SysConfig.etlKtrPath + transName + ".ktr";//转换生成的配置文件的路径
            File file = new File(transFilePath);
            FileUtils.writeStringToFile(file, transXml, "UTF-8");

            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        }

        return false;
    }


    /**
     * 生成kjb文件
     *
     * @param etlId 作业名称
     * @return
     */
    public static boolean genKjbFile(String etlId, EtlEntity entity) {
        try {
            KettleEnvironment.init();
            JobMeta jobMeta = new JobMeta();
            jobMeta.setName(etlId);
            JobEntrySpecial start = new JobEntrySpecial();
            start.setName("START");

            start.setRepeat(entity.getRepeat() == 0 ? false : true);
            start.setSchedulerType(entity.getScheduleType());

            switch (entity.getScheduleType()) {
                case 1:
                    start.setIntervalSeconds(entity.getIntervalSecond());
                    start.setIntervalMinutes(entity.getIntervalMinute());
                    break;
                case 2:
                    start.setHour(entity.getFixedHour());
                    start.setMinutes(entity.getFixedMinute());
                    break;
                case 3:
                    start.setHour(entity.getFixedHour());
                    start.setMinutes(entity.getFixedMinute());
                    start.setWeekDay(entity.getFixedWeekday());
                    break;
                case 4:
                    start.setHour(entity.getFixedHour());
                    start.setMinutes(entity.getFixedMinute());
                    start.setDayOfMonth(entity.getFixedDay());
                    break;
            }

            start.setStart(true);
            JobEntryCopy startEntry = new JobEntryCopy(start);
            startEntry.setDrawn(true);
            startEntry.setLocation(200, 100);
            jobMeta.addJobEntry(startEntry);

            JobEntryWriteToFile writeToFile = new JobEntryWriteToFile();
            writeToFile.setName("转换");
            writeToFile.setLogLevel(LogLevel.BASIC);
            writeToFile.setTypeId("TRANS");
            writeToFile.setFilename(SysConfig.etlKtrPath + etlId + ".ktr");

            JobEntryCopy writeToLogEntry = new JobEntryCopy(writeToFile);
            writeToLogEntry.setDrawn(true);
            writeToLogEntry.setLocation(400, 100);
            jobMeta.addJobEntry(writeToLogEntry);
            jobMeta.addJobHop(new JobHopMeta(startEntry, writeToLogEntry));

            String transXml = jobMeta.getXML();
            File file = new File(SysConfig.etlKjbPath + etlId + ".kjb");
            FileUtils.writeStringToFile(file, transXml, "UTF-8");
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return false;
    }

    /**
     * 启动任务
     *
     * @param etlId
     * @throws Exception
     */
    public static void startEtlJob(final String etlId, String entityId) throws KettleException {

        //job.waitUntilFinished();

        Job jobTemp = jobMap.get(entityId);
        if (jobTemp == null || !jobTemp.isAlive()) {
            KettleEnvironment.init();//初始化
            JobMeta jobMeta = new JobMeta(SysConfig.etlKjbPath + etlId + ".kjb", null);
            Job job = new Job(null, jobMeta);
            job.start();
            jobMap.put(entityId, job);
        }
    }

    /**
     * 停止任务
     *
     * @throws Exception
     */
    public static void stopEtlJob(String entityId) throws KettleException {
        Job jobTemp = jobMap.get(entityId);
        if (jobTemp != null && jobTemp.isAlive()) {
            KettleEnvironment.init();//初始化
            jobTemp.stopAll();
            jobMap.remove(entityId);
        }
    }
}

