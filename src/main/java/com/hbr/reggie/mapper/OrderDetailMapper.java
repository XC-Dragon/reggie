package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName OrdersDetailMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 23:32
 * @Version 1.0
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
