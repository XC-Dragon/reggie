package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName EmployeeMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/8 20:09
 * @Version 1.0
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
