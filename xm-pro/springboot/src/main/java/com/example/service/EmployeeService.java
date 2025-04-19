package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.exception.CustomException;
import com.example.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    public void add(Employee employee) {
        String username = employee.getUsername();  // 账号
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee != null) {  // 注册的账号已存在  无法注册
            throw new CustomException("500", "账号已存在，请更换别的账号");
        }
        Employee dbEmployee1 = employeeMapper.selectByNo(employee.getNo());
        if (dbEmployee1 != null) {  // 工号是唯一的
            throw new CustomException("500", "工号已存在，请再次确认是否正确");
        }
        if (StrUtil.isBlank(employee.getPassword())) {  // 密码没填
            employee.setPassword("123");  // 默认密码 123
        }
        if (StrUtil.isBlank(employee.getName())) {  // 名字没填
            employee.setName(employee.getUsername());  // 默认名称
        }
        // 一定要设置角色
        employee.setRole("EMP");  // 员工的角色
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public List<Employee> selectAll(Employee employee) {
        // 额外的业务操作
        return employeeMapper.selectAll(employee);
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    public Employee login(Account account) {
        String username = account.getUsername();  // 账号
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee == null) {  // 没查询到任何用户   说明没有这个账号
            throw new CustomException("500", "账号不存在");
        }
        // 数据库存在这个账号
        String password = account.getPassword();
        if (!dbEmployee.getPassword().equals(password)) {  // 用户输入的密码跟数据库账号的密码不匹配
            throw new CustomException("500", "账号或密码错误");
        }
        return dbEmployee;
    }


    public void register(Employee employee) {
        this.add(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee = this.selectById(id);
        if (!employee.getPassword().equals(account.getPassword())) {  // 页面传来的原密码跟数据库密码对比  不匹配就报错
            throw new CustomException("500", "对不起，原密码错误");
        }
        employee.setPassword(account.getNewPassword());  // 设置新密码
        this.update(employee);
    }
}
