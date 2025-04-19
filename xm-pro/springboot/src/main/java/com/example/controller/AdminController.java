package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 新增数据
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    /**
     * 更新数据
     */
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }

    /**
     * 删除单个数据
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询单个数据
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }


    /**
     * 查询所有的数据
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    /**
     * 分页查询的数据
     * pageNum: 当前页码
     * pageSize: 每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> pageInfo = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
