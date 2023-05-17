package com.hbr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbr.reggie.pojo.Orders;

/**
 * @ClassName OrdersService
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 23:33
 * @Version 1.0
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
