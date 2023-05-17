package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName UserMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 12:30
 * @Version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
