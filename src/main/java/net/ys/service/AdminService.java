package net.ys.service;

import net.ys.bean.EtlAdmin;
import net.ys.dao.AdminDao;
import net.ys.utils.Tools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {
    @Resource
    private AdminDao adminDao;

    public EtlAdmin queryAdmin(String username, String password) {
        String pass = Tools.genMD5(password);
        return adminDao.queryAdmin(username, pass);
    }
}
