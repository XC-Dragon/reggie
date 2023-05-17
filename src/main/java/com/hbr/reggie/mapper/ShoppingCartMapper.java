package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName ShoppingCartMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 22:45
 * @Version 1.0
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
