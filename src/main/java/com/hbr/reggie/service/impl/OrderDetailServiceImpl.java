package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.mapper.OrderDetailMapper;
import com.hbr.reggie.pojo.OrderDetail;
import com.hbr.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderDetailServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 23:34
 * @Version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
