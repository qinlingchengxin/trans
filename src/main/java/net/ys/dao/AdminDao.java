package net.ys.dao;

import net.ys.bean.EtlAdmin;
import net.ys.mapper.AdminMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AdminDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public EtlAdmin queryAdmin(String username, String pass) {
        String sql = "SELECT id, mag_type, username, password FROM sys_etl_admin WHERE username = ? AND password =?";
        List<EtlAdmin> list = jdbcTemplate.query(sql, new AdminMapper(), username, pass);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
