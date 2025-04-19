package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        String username = admin.getUsername();  // 账号
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) {  // 注册的账号已存在  无法新增
            throw new CustomException("500", "账号已存在，请更换别的账号");
        }
        if (StrUtil.isBlank(admin.getPassword())) {  // 密码没填
            admin.setPassword("admin");  // 默认密码 admin
        }
        if (StrUtil.isBlank(admin.getName())) {  // 名字没填
            admin.setName(admin.getUsername());  // 默认名称
        }
        // 一定要设置角色
        admin.setRole("ADMIN");  // 管理员的角色
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll(Admin admin) {
        // 额外的业务操作
        return adminMapper.selectAll(admin);
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public Admin login(Account account) {
        String username = account.getUsername();  // 账号
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) {  // 没查询到任何用户   说明没有这个账号
            throw new CustomException("500", "账号不存在");
        }
        // 数据库存在这个账号
        String password = account.getPassword();
        if (!dbAdmin.getPassword().equals(password)) {  // 用户输入的密码跟数据库账号的密码不匹配
            throw new CustomException("500", "账号或密码错误");
        }
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if (!admin.getPassword().equals(account.getPassword())) {  // 页面传来的原密码跟数据库密码对比  不匹配就报错
            throw new CustomException("500", "对不起，原密码错误");
        }
        admin.setPassword(account.getNewPassword());  // 设置新密码
        this.update(admin);
    }

}
