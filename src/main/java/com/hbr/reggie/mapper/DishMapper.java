package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName DishMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 18:23
 * @Version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
