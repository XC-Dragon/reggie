package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hbr.reggie.common.BaseContext;
import com.hbr.reggie.common.R;
import com.hbr.reggie.pojo.ShoppingCart;
import com.hbr.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ShoppingCartController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 23:03
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车信息：{}", shoppingCart.toString());
        // 设置用户id，制定当前时那个用户的购物车数据
        shoppingCart.setUserId(BaseContext.getUserId());

        LambdaQueryWrapper<ShoppingCart> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getUserId());
        // 查询当前菜品或套餐是否已经存在购物车中
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            dishLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            dishLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(dishLambdaQueryWrapper);

        // 如果已经存在，则修改数量
        if (shoppingCartServiceOne != null) {
            shoppingCartServiceOne.setNumber(shoppingCartServiceOne.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            // 如果不存在，则新增
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        }
        return R.success(shoppingCartServiceOne);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (dishId != null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        Integer number = shoppingCartServiceOne.getNumber();
        if (number > 1) {
            shoppingCartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        }
        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查询购物车信息");
        LambdaQueryWrapper<ShoppingCart> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getUserId());
        dishLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(dishLambdaQueryWrapper);
        return R.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getUserId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("清空购物车成功");
    }
}
