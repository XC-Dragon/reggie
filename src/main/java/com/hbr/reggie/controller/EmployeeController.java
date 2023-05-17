package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbr.reggie.common.R;
import com.hbr.reggie.pojo.Employee;
import com.hbr.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName EmployeeController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/8 20:12
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1.将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2.根据用户名和密码查询用户信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(wrapper);
        // 3.如果没有查询到用户信息，返回登录失败
        if (one == null) {
            return R.error("登录失败");
        }
        // 4.密码比对，如果密码不一致，返回用户名或密码错误
        if (!one.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
        }
        // 5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (one.getStatus() == 0) {
            return R.error("员工已禁用");
        }
        // 6.登录成功，将用户信息保存到session中
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("保存员工信息：{}", employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);
        return R.success("保存成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(Integer page, Integer pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 1.创建查询条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 2.设置查询条件
        wrapper.like(StringUtils.isNotBlank(name), Employee::getName, name);
        // 3.添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        // 4.执行分页查询
        employeeService.page(pageInfo, wrapper);
        // 5.返回结果
        return R.success(pageInfo);
    }

    @PutMapping
    public R<Employee> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("修改员工信息：{}", employee.toString());

        Long id = (Long) request.getSession().getAttribute("employee");

        employeeService.updateById(employee);
        return R.success(employee);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("员工不存在");
        }
        return R.success(employee);
    }
}
