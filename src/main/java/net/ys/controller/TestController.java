package net.ys.controller;

import net.ys.constant.GenResult;
import net.ys.service.DbService;
import net.ys.utils.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "test")
public class TestController {

    @Resource
    private DbService dbService;

    @RequestMapping(value = "genTable")
    @ResponseBody
    public Map<String, Object> genTable() {
        try {
            long now = System.currentTimeMillis();
            dbService.genTable();
            return GenResult.SUCCESS.genResult(System.currentTimeMillis() - now);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }

    @RequestMapping(value = "genField")
    @ResponseBody
    public Map<String, Object> genField() {
        try {
            long now = System.currentTimeMillis();
            dbService.genField();
            return GenResult.SUCCESS.genResult(System.currentTimeMillis() - now);
        } catch (Exception e) {
            LogUtil.error(e);
            return GenResult.UNKNOWN_ERROR.genResult();
        }
    }
}
