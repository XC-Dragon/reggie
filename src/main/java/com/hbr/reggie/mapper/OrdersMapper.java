package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName OrderMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 23:31
 * @Version 1.0
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
