package com.hbr.reggie.dto;

import com.hbr.reggie.pojo.OrderDetail;
import com.hbr.reggie.pojo.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName OrderDto
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/15 18:12
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;
}
