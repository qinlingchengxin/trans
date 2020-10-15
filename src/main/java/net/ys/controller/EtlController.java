package net.ys.controller;

import net.ys.bean.*;
import net.ys.constant.DbType;
import net.ys.constant.GenResult;
import net.ys.constant.SysRegex;
import net.ys.service.EtlService;
import net.ys.utils.KettleUtil;
import net.ys.utils.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "web/etl")
public class EtlController {

    @Resource
    private EtlService etlService;

    @RequestMapping(value = "dataSourceList")
    public ModelAndView dataSourceList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("etlDataSource/list");
        if (page < 1) {
            page = 1;
        }
        long count = etlService.queryEtlDataSourceCount();

        long t = count / pageSize;
        int k = count % pageSize == 0 ? 0 : 1;
        int totalPage = (int) (t + k);

        if (page > totalPage && count > 0) {
            page = totalPage;
        }

        List<EtlDataSource> dataSources;
        if ((page - 1) * pageSize < count) {
            dataSources = etlService.queryEtlDataSources(page, pageSize);
        } else {
            dataSources = new ArrayList<EtlDataSource>();
        }

        modelAndView.addObject("count", count);
        modelAndView.addObject("currPage", page);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("dataSources", dataSources);
        return modelAndView;
    }

    @RequestMapping(value = "dataSourceEdit")
    public ModelAndView dataSourceEdit(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "0") int isView) {
        ModelAndView modelAndView = new ModelAndView("etlDataSource/edit");
        EtlDataSource dataSource;
        if ("".equals(id)) {
            dataSource = new EtlDataSource();
        } else {
            dataSource = etlService.queryEtlDataSource(id);
        }

        modelAndView.addObject("dataSource", dataSource);
        modelAndView.addObject("isView", isView);
        return modelAndView;
    }

    @RequestMapping(value = "dataSourceSave")
    @ResponseBody
    public Map<String, Object> dataSourceSave(EtlDataSource etlDataSource) {
        boolean flag = etlService.updateEtlDataSource(etlDataSource);
        if (!flag) {
            return GenResult.FAILED.genResult();
        }

        if (etlDataSource.getDbType() == DbType.KING_BASE.type && StringUtils.isBlank(etlDataSource.getDbSchema())) {
            return GenResult.PARAMS_ERROR.genResult();
        }

        /**
         * 异步获取表和字段信息
         */
        etlService.genTablesAndFields(etlDataSource);

        return GenResult.SUCCESS.genResult();
    }

    @RequestMapping(value = "dataSourceAdd")
    @ResponseBody
    public Map<String, Object> dataSourceAdd(EtlDataSource etlDataSource) {
        try {
            etlDataSource = etlService.addEtlDataSource(etlDataSource);
            if (etlDataSource == null) {
                return GenResult.FAILED.genResult();
            }

            if (etlDataSource.getDbType() == DbType.KING_BASE.type && StringUtils.isBlank(etlDataSource.getDbSchema())) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            /**
             * 异步获取表和字段信息
             */
            etlService.genTablesAndFields(etlDataSource);

            return GenResult.SUCCESS.genResult(etlDataSource);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "etlAllTables")
    public ModelAndView etlAllTables(@RequestParam(defaultValue = "") String dsId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("etlAllTable/list");
        if (page < 1) {
            page = 1;
        }
        long count = etlService.queryEtlAllTableCount(dsId);

        long t = count / pageSize;
        int k = count % pageSize == 0 ? 0 : 1;
        int totalPage = (int) (t + k);

        if (page > totalPage && count > 0) {
            page = totalPage;
        }

        List<EtlAllTable> etlAllTables;
        if ((page - 1) * pageSize < count) {
            etlAllTables = etlService.queryEtlAllTables(dsId, page, pageSize);
        } else {
            etlAllTables = new ArrayList<EtlAllTable>();
        }

        modelAndView.addObject("count", count);
        modelAndView.addObject("currPage", page);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("etlAllTables", etlAllTables);
        modelAndView.addObject("dsId", dsId);
        return modelAndView;
    }

    @RequestMapping(value = "syncTableField")
    @ResponseBody
    public Map<String, Object> syncTableField(@RequestParam(defaultValue = "") String dsId) {
        try {

            if ("".equals(dsId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            EtlDataSource etlDataSource = etlService.queryEtlDataSource(dsId);

            boolean flag = etlService.genTables(etlDataSource);
            if (flag) {
                flag = etlService.genFields(etlDataSource);
            }

            if (!flag) {
                return GenResult.FAILED.genResult();
            }
            return GenResult.SUCCESS.genResult(etlDataSource);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "testConnDs")
    @ResponseBody
    public Map<String, Object> testConnDs(EtlDataSource etlDataSource) {
        try {
            boolean flag = etlService.testConnDs(etlDataSource);
            if (!flag) {
                return GenResult.FAILED.genResult();
            }
            return GenResult.SUCCESS.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "projectList")
    public ModelAndView projectList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("etlProject/list");
        if (page < 1) {
            page = 1;
        }
        long count = etlService.queryEtlProjectCount();

        long t = count / pageSize;
        int k = count % pageSize == 0 ? 0 : 1;
        int totalPage = (int) (t + k);

        if (page > totalPage && count > 0) {
            page = totalPage;
        }

        List<EtlProject> etlProjects;
        if ((page - 1) * pageSize < count) {
            etlProjects = etlService.queryEtlProjects(page, pageSize);
        } else {
            etlProjects = new ArrayList<EtlProject>();
        }

        modelAndView.addObject("count", count);
        modelAndView.addObject("currPage", page);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("etlProjects", etlProjects);
        return modelAndView;
    }

    @RequestMapping(value = "projectEdit")
    public ModelAndView projectEdit(@RequestParam(defaultValue = "") String id) {
        ModelAndView modelAndView = new ModelAndView("etlProject/edit");
        EtlProject etlProject;
        if ("".equals(id)) {//新增
            etlProject = new EtlProject();
        } else {
            etlProject = etlService.queryEtlProject(id);
        }

        List<EtlDataSource> dataSources = etlService.queryEtlDataSources(1, Integer.MAX_VALUE);
        modelAndView.addObject("etlProject", etlProject);
        modelAndView.addObject("dataSources", dataSources);
        return modelAndView;
    }

    @RequestMapping(value = "projectSave")
    @ResponseBody
    public Map<String, Object> projectSave(EtlProject etlProject) {
        boolean flag = etlService.updateEtlProject(etlProject);
        if (!flag) {
            return GenResult.FAILED.genResult();
        }
        return GenResult.SUCCESS.genResult();
    }

    @RequestMapping(value = "projectDel")
    @ResponseBody
    public Map<String, Object> projectDel(@RequestParam(defaultValue = "") String id) {
        boolean flag = etlService.projectDel(id);
        if (!flag) {
            return GenResult.FAILED.genResult();
        }
        return GenResult.SUCCESS.genResult();
    }

    @RequestMapping(value = "projectAdd")
    @ResponseBody
    public Map<String, Object> projectAdd(EtlProject etlProject) {
        try {

            etlProject = etlService.addEtlProject(etlProject);
            if (etlProject == null) {
                return GenResult.FAILED.genResult();
            }
            return GenResult.SUCCESS.genResult(etlProject);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "entityList")
    public ModelAndView entityList(@RequestParam(defaultValue = "") String prjId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("etlEntity/list");
        if (page < 1) {
            page = 1;
        }
        long count = etlService.queryEtlEntityCount(prjId);

        long t = count / pageSize;
        int k = count % pageSize == 0 ? 0 : 1;
        int totalPage = (int) (t + k);

        if (page > totalPage && count > 0) {
            page = totalPage;
        }

        List<EtlEntity> etlEntities;
        if ((page - 1) * pageSize < count) {
            etlEntities = etlService.queryEtlEntities(prjId, page, pageSize);
        } else {
            etlEntities = new ArrayList<EtlEntity>();
        }

        modelAndView.addObject("count", count);
        modelAndView.addObject("currPage", page);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("etlEntities", etlEntities);
        modelAndView.addObject("prjId", prjId);
        return modelAndView;
    }

    @RequestMapping(value = "entityEdit")
    public ModelAndView entityEdit(@RequestParam(defaultValue = "") String prjId, @RequestParam(defaultValue = "") String id) {
        ModelAndView modelAndView = new ModelAndView("etlEntity/edit");
        EtlEntity etlEntity;

        List<EtlAllField> srcFields;
        List<EtlAllField> desFields;

        EtlProject project = etlService.queryEtlProject(prjId);
        List<EtlAllTable> srcTables = etlService.querySrcTables(project);
        List<EtlAllTable> desTables = etlService.queryDesTables(project);

        if ("".equals(id)) {//新增
            etlEntity = new EtlEntity();
            etlEntity.setPrjId(prjId);
            if (srcTables.size() > 0) {
                srcFields = etlService.queryFields(srcTables.get(0));
            } else {
                srcFields = new ArrayList<EtlAllField>();
            }

            if (desTables.size() > 0) {
                desFields = etlService.queryFields(desTables.get(0));
            } else {
                desFields = new ArrayList<EtlAllField>();
            }
        } else {
            etlEntity = etlService.queryEtlEntity(id);

            EtlAllTable srcTable = etlService.querySrcEtlAllTable(prjId, etlEntity.getSrcTabName());
            EtlAllTable desTable = etlService.queryDesEtlAllTable(prjId, etlEntity.getDesTabName());

            if (srcTable != null) {
                srcFields = etlService.queryFields(srcTable);
            } else {
                srcFields = new ArrayList<EtlAllField>();
            }
            if (desTable != null) {
                desFields = etlService.queryFields(desTable);
            } else {
                desFields = new ArrayList<EtlAllField>();
            }
        }

        if (StringUtils.isBlank(etlEntity.getSrcTabId())) {
            if (srcTables.size() > 0) {
                etlEntity.setSrcTabId(srcTables.get(0).getId());
            }
        }

        if (StringUtils.isBlank(etlEntity.getDesTabId())) {
            if (desTables.size() > 0) {
                etlEntity.setDesTabId(desTables.get(0).getId());
            }
        }

        modelAndView.addObject("etlEntity", etlEntity);
        modelAndView.addObject("srcTables", srcTables);
        modelAndView.addObject("desTables", desTables);
        modelAndView.addObject("srcFields", srcFields);
        modelAndView.addObject("desFields", desFields);
        return modelAndView;
    }

    @RequestMapping(value = "primaryKey")
    @ResponseBody
    public Map<String, Object> primaryKey(@RequestParam(defaultValue = "") String prjId, @RequestParam(defaultValue = "") String tableName, @RequestParam(defaultValue = "0") int type) {
        List<EtlAllField> pks;
        if (type == 0) {//原表
            EtlAllTable srcTable = etlService.querySrcEtlAllTable(prjId, tableName);
            pks = etlService.queryFields(srcTable);

        } else {//目标表
            EtlAllTable desTable = etlService.queryDesEtlAllTable(prjId, tableName);
            pks = etlService.queryFields(desTable);
        }
        return GenResult.SUCCESS.genResult(pks);
    }

    @RequestMapping(value = "entitySave")
    @ResponseBody
    public Map<String, Object> entitySave(EtlEntity etlEntity) {

        if (StringUtils.isBlank(etlEntity.getSrcPrimaryKey()) || StringUtils.isBlank(etlEntity.getDesPrimaryKey())) {
            return GenResult.NO_PRI_KEY.genResult();
        }

        boolean flag = etlService.updateEtlEntity(etlEntity);
        if (!flag) {
            return GenResult.FAILED.genResult();
        }
        return GenResult.SUCCESS.genResult();
    }

    @RequestMapping(value = "entityDel")
    @ResponseBody
    public Map<String, Object> entityDel(@RequestParam(defaultValue = "") String id) {
        boolean flag = etlService.entityDel(id);
        if (!flag) {
            return GenResult.FAILED.genResult();
        }
        return GenResult.SUCCESS.genResult();
    }

    @RequestMapping(value = "entityAdd")
    @ResponseBody
    public Map<String, Object> entityAdd(EtlEntity etlEntity) {
        try {

            if (StringUtils.isBlank(etlEntity.getSrcPrimaryKey()) || StringUtils.isBlank(etlEntity.getDesPrimaryKey())) {
                return GenResult.NO_PRI_KEY.genResult();
            }

            if (StringUtils.isBlank(etlEntity.getSrcTabName())) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            if (!etlEntity.getDesTabName().matches(SysRegex.TABLE_FIELD_NAME.regex)) {
                return GenResult.TABLE_NAME_INVALID.genResult();
            }

            etlEntity = etlService.addEtlEntity(etlEntity);
            if (etlEntity == null) {
                return GenResult.FAILED.genResult();
            }
            return GenResult.SUCCESS.genResult(etlEntity);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "fieldList")
    public ModelAndView fieldList(@RequestParam(defaultValue = "") String entityId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("etlField/list");
        if (page < 1) {
            page = 1;
        }
        long count = etlService.queryEtlFieldCount(entityId);

        long t = count / pageSize;
        int k = count % pageSize == 0 ? 0 : 1;
        int totalPage = (int) (t + k);

        if (page > totalPage && count > 0) {
            page = totalPage;
        }

        List<EtlField> etlFields;
        if ((page - 1) * pageSize < count) {
            etlFields = etlService.queryEtlFields(entityId, page, pageSize);
        } else {
            etlFields = new ArrayList<EtlField>();
        }

        modelAndView.addObject("count", count);
        modelAndView.addObject("currPage", page);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("etlFields", etlFields);
        modelAndView.addObject("entityId", entityId);
        return modelAndView;
    }

    /**
     * 新增界面
     *
     * @param entityId
     * @return
     */
    @RequestMapping(value = "fieldEdit")
    public ModelAndView fieldEdit(@RequestParam(defaultValue = "") String entityId) {
        ModelAndView modelAndView = new ModelAndView("etlField/edit");
        EtlField etlField = new EtlField();
        etlField.setEntityId(entityId);
        EtlProject project = etlService.queryEtlKtrProject(entityId);
        EtlEntity entity = etlService.queryEtlEntity(entityId);

        EtlAllTable srcTable = etlService.querySrcEtlAllTable(project.getId(), entity.getSrcTabName());
        EtlAllTable desTable = etlService.queryDesEtlAllTable(project.getId(), entity.getDesTabName());

        List<EtlAllField> srcFields = etlService.queryFields(srcTable);
        List<EtlAllField> desFields = etlService.queryFields(desTable);

        modelAndView.addObject("etlField", etlField);
        modelAndView.addObject("srcFields", srcFields);
        modelAndView.addObject("desFields", desFields);
        return modelAndView;
    }

    /**
     * 添加所有
     *
     * @param entityId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addAll")
    public Map<String, Object> addAll(@RequestParam(defaultValue = "") String entityId) {
        try {
            EtlProject project = etlService.queryEtlKtrProject(entityId);
            EtlEntity entity = etlService.queryEtlEntity(entityId);

            EtlAllTable srcTable = etlService.querySrcEtlAllTable(project.getId(), entity.getSrcTabName());

            List<EtlAllField> srcFields = etlService.queryFields(srcTable);
            for (EtlAllField srcField : srcFields) {
                EtlField etlField = new EtlField();
                etlField.setEntityId(entityId);
                etlField.setSrcFieldName(srcField.getName());
                etlField.setDesFieldName(srcField.getName());
                etlService.addEtlField(etlField);
            }

            return GenResult.SUCCESS.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "fieldAdd")
    @ResponseBody
    public Map<String, Object> fieldAdd(EtlField etlField) {
        try {

            if (StringUtils.isBlank(etlField.getSrcFieldName())) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            if (!etlField.getDesFieldName().matches(SysRegex.TABLE_FIELD_NAME.regex)) {
                return GenResult.TABLE_NAME_INVALID.genResult();
            }

            etlField = etlService.addEtlField(etlField);
            if (etlField == null) {
                return GenResult.FAILED.genResult();
            }
            return GenResult.SUCCESS.genResult(etlField);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "fieldDel")
    @ResponseBody
    public Map<String, Object> fieldDel(@RequestParam(defaultValue = "") String id) {
        try {
            boolean flag = etlService.fieldDel(id);
            if (!flag) {
                return GenResult.FAILED.genResult();
            }
            return GenResult.SUCCESS.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "startEtlJob")
    @ResponseBody
    public Map<String, Object> startEtlJob(HttpServletRequest request, @RequestParam(defaultValue = "") String entityId) {
        try {

            if ("".equals(entityId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }
            List<EtlField> fields = etlService.queryEtlKtrFields(entityId);
            if (fields.size() == 0) {
                return GenResult.NO_MAP_FIELD.genResult();
            }

            EtlEntity entity = etlService.queryEtlEntity(entityId);

            if (entity.getExec() == 1) {
                return GenResult.EXEC_ING.genResult();
            }

            if (entity.getSrcTabId().equals(entity.getDesTabId())) {
                return GenResult.CAN_NOT_ETL_TRANS.genResult();
            }

            EtlProject project = etlService.queryEtlKtrProject(entityId);
            DbSchema dbSchema = etlService.queryDbSchema(project);
            String etlId = entity.getEtlId();
            boolean flag = KettleUtil.genKtrFile(entity, project, fields, etlId, dbSchema);//生成ktr转换文件

            if (flag) {
                flag = KettleUtil.genKjbFile(etlId, entity);
                if (flag) {//执行job
                    KettleUtil.startEtlJob(etlId, entityId);
                    flag = etlService.chgJobStatus(entityId, 1);
                }
            }

            if (!flag) {
                return GenResult.FAILED.genResult();
            }

            return GenResult.SUCCESS.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "stopEtlJob")
    @ResponseBody
    public Map<String, Object> stopEtlJob(@RequestParam(defaultValue = "") String entityId) {
        try {

            if ("".equals(entityId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            EtlEntity entity = etlService.queryEtlEntity(entityId);

            if (entity.getExec() == 0) {
                return GenResult.SUCCESS.genResult();
            }

            KettleUtil.stopEtlJob(entityId);
            boolean flag = etlService.chgJobStatus(entityId, 0);
            if (flag) {
                return GenResult.SUCCESS.genResult();
            }

            return GenResult.FAILED.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "startApiJob")
    @ResponseBody
    public Map<String, Object> startApiJob(HttpServletRequest request, @RequestParam(defaultValue = "") String entityId) {
        try {

            if ("".equals(entityId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }
            List<EtlField> fields = etlService.queryEtlKtrFields(entityId);
            if (fields.size() == 0) {
                return GenResult.NO_MAP_FIELD.genResult();
            }

            EtlEntity entity = etlService.queryEtlEntity(entityId);

            if (entity.getApiExec() == 1) {
                return GenResult.EXEC_ING.genResult();
            }

            EtlProject project = etlService.queryEtlKtrProject(entityId);

            DbSchema dbSchema = etlService.queryDbSchema(project);

            /**
             * 添加到定时任务
             */
            boolean flag = etlService.startApiJob(project, entity, fields, dbSchema);

            /**
             * 执行job
             */
            if (flag && entity.getRepeat() == 1) {
                flag = etlService.chgApiJobStatus(entityId, 1);
            }

            if (!flag) {
                return GenResult.FAILED.genResult();
            }

            return GenResult.SUCCESS.genResult(entity.getRepeat());
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "stopApiJob")
    @ResponseBody
    public Map<String, Object> stopApiJob(@RequestParam(defaultValue = "") String entityId) {
        try {

            if ("".equals(entityId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            EtlEntity entity = etlService.queryEtlEntity(entityId);

            if (entity.getApiExec() == 0) {
                return GenResult.SUCCESS.genResult();
            }
            boolean flag = etlService.stopApiJob(entityId);
            if (flag) {
                flag = etlService.chgApiJobStatus(entityId, 0);
            }

            if (flag) {
                return GenResult.SUCCESS.genResult();
            }

            return GenResult.FAILED.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "stopAllEtlJob")
    @ResponseBody
    public Map<String, Object> stopAllEtlJob(@RequestParam(defaultValue = "") String prjId) {
        try {

            if ("".equals(prjId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            List<EtlEntity> etlEntities = etlService.queryEtlEntities(prjId, 1, Integer.MAX_VALUE);

            for (EtlEntity etlEntity : etlEntities) {
                if (etlEntity.getExec() == 0) {
                    continue;
                }
                KettleUtil.stopEtlJob(etlEntity.getId());
            }

            boolean flag = etlService.stopAllEtlJob(prjId);
            if (flag) {
                return GenResult.SUCCESS.genResult();
            }

            return GenResult.FAILED.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "stopAllApiJob")
    @ResponseBody
    public Map<String, Object> stopAllApiJob(@RequestParam(defaultValue = "") String prjId) {
        try {

            if ("".equals(prjId)) {
                return GenResult.PARAMS_ERROR.genResult();
            }

            List<EtlEntity> etlEntities = etlService.queryEtlEntities(prjId, 1, Integer.MAX_VALUE);

            for (EtlEntity etlEntity : etlEntities) {
                if (etlEntity.getApiExec() == 0) {
                    continue;
                }
                etlService.stopApiJob(etlEntity.getId());
            }

            boolean flag = etlService.stopAllApiJob(prjId);
            if (flag) {
                return GenResult.SUCCESS.genResult();
            }

            return GenResult.FAILED.genResult();
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }
}
