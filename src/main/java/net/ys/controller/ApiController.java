package net.ys.controller;

import net.sf.jxls.transformer.XLSTransformer;
import net.ys.bean.DbField;
import net.ys.bean.EtlDataSource;
import net.ys.service.EtlService;
import net.ys.utils.DBUtil;
import net.ys.utils.LogUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "api")
public class ApiController {

    @Resource
    private EtlService etlService;

    @RequestMapping("doc")
    public void doc(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "") String dsId) {

        ServletOutputStream os = null;
        InputStream is = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            EtlDataSource dataSource = etlService.queryEtlDataSource(dsId);
            List<DbField> fields = DBUtil.getAllFieldsMySql(dataSource.getDbIp(), dataSource.getDbPort(), dataSource.getDbName(), dataSource.getDbUsername(), dataSource.getDbPwd());
            map.put("records", fields);
            String templateFileName = request.getSession().getServletContext().getRealPath("/template/doc.xls");
            String resultFileName = "doc-" + System.currentTimeMillis() + ".xls";
            os = response.getOutputStream();
            is = new BufferedInputStream(new FileInputStream(templateFileName));
            XLSTransformer transformer = new XLSTransformer();
            Workbook wb = transformer.transformXLS(is, map);
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + resultFileName);
            response.setContentType("application/msexcel");
            wb.write(os);
            is.close();
            os.close();
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
