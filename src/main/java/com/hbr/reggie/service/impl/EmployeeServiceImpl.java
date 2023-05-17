package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.mapper.EmployeeMapper;
import com.hbr.reggie.pojo.Employee;
import com.hbr.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @ClassName EmployeeServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/8 20:11
 * @Version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
