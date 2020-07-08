package net.ys.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 系统配置类
 * User: NMY
 * Date: 16-8-28
 */
@Component
public class SysConfig {

    public static String etlKtrPath;

    public static String etlKjbPath;

    @Value("${etl_ktr_path}")
    public void setEtlKtrPath(String etlKtrPath) {
        File file = new File(etlKtrPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("etlKtrPath is invalid");
            }
        }
        String absolutePath = file.getAbsolutePath();
        this.etlKtrPath = absolutePath.replaceAll("\\\\", "/") + "/";
    }

    @Value("${etl_kjb_path}")
    public void setEtlKjbPath(String etlKjbPath) {
        File file = new File(etlKjbPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("etlKjbPath is invalid");
            }
        }
        String absolutePath = file.getAbsolutePath();
        this.etlKjbPath = absolutePath.replaceAll("\\\\", "/") + "/";
    }
}
