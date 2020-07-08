package net.ys.listener;

import net.ys.component.ApplicationContextUtil;
import net.ys.service.EtlService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * User: NMY
 * Date: 16-8-29
 */
@WebListener
public class SystemListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EtlService etlService = ApplicationContextUtil.getBean("etlService", EtlService.class);
        etlService.restartEtlJob();
        etlService.restartApiJob();
        System.out.println("system contextInitialized");
    }
}