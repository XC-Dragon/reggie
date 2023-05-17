package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbr.reggie.common.BaseContext;
import com.hbr.reggie.common.CustomException;
import com.hbr.reggie.common.R;
import com.hbr.reggie.dto.OrderDto;
import com.hbr.reggie.pojo.OrderDetail;
import com.hbr.reggie.pojo.Orders;
import com.hbr.reggie.pojo.ShoppingCart;
import com.hbr.reggie.service.OrderDetailService;
import com.hbr.reggie.service.OrdersService;
import com.hbr.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OrderController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 23:36
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrdersService ordersService;
    private final OrderDetailService orderDetailService;
    private final ShoppingCartService shoppingCartService;

    public OrderController(OrdersService ordersService, OrderDetailService orderDetailService, ShoppingCartService shoppingCartService) {
        this.ordersService = ordersService;
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}", orders.toString());
        try {
            ordersService.submit(orders);
        } catch (CustomException e) {
            return R.error(e.getMessage());
        }
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrderDto>> userPage(Integer page, Integer pageSize){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getUserId());
        ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersPage, ordersLambdaQueryWrapper);

        List<OrderDto> collect = ordersPage.getRecords().stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetail = orderDetailService.list(orderDetailLambdaQueryWrapper);
            orderDto.setOrderDetails(orderDetail);
            return orderDto;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(ordersPage, orderDtoPage, "records");
        orderDtoPage.setRecords(collect);
        log.info("订单数据：{}", orderDtoPage);
        return R.success(orderDtoPage);
    }

    @GetMapping("/page")
    public R<Page<OrderDto>> page(Integer page, Integer pageSize, String number, String beginTime, String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        ordersLambdaQueryWrapper.like(StringUtils.isNotBlank(number), Orders::getId, number);
        ordersLambdaQueryWrapper.gt(StringUtils.isNotBlank(beginTime), Orders::getOrderTime, beginTime);
        ordersLambdaQueryWrapper.lt(StringUtils.isNotBlank(endTime), Orders::getOrderTime, endTime);
        ordersService.page(ordersPage, ordersLambdaQueryWrapper);
        List<OrderDto> orderDtoList = ordersPage.getRecords().stream().map(orders -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(orders, orderDto);
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, orders.getId());
            List<OrderDetail> list = orderDetailService.list(orderDetailLambdaQueryWrapper);
            orderDto.setOrderDetails(list);
            return orderDto;
        }).collect(Collectors.toList());
        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage, orderDtoPage, "records");
        orderDtoPage.setRecords(orderDtoList);
        return R.success(orderDtoPage);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody HashMap<String, Object> map) {
        if (map.isEmpty()){
            return R.error("参数错误");
        }
        Integer status = (Integer) map.get("status");
        String id = (String) map.get("id");
        log.info("订单状态：{}, id：{}", status, id);
        LambdaUpdateWrapper<Orders> ordersLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        ordersLambdaUpdateWrapper.eq(Orders::getId, id);
        ordersLambdaUpdateWrapper.set(Orders::getStatus, status);
        ordersService.update(ordersLambdaUpdateWrapper);
        return R.success("派送成功");
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, orders.getId());
        List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(BaseContext.getUserId());
            return shoppingCart;
        }).collect(Collectors.toList());
        shoppingCartService.saveBatch(shoppingCartList);
        return R.success("下单成功");
    }
}
