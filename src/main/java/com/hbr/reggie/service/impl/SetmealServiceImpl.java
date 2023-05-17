package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.common.CustomException;
import com.hbr.reggie.dto.SetmealDto;
import com.hbr.reggie.mapper.SetmealMapper;
import com.hbr.reggie.pojo.Setmeal;
import com.hbr.reggie.pojo.SetmealDish;
import com.hbr.reggie.service.SetmealDishService;
import com.hbr.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SetmealServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 18:29
 * @Version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    private final SetmealDishService setmealDishService;

    public SetmealServiceImpl(SetmealDishService setmealDishService) {
        this.setmealDishService = setmealDishService;
    }

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().peek(setmealDish -> setmealDish.setSetmealId(setmealDto.getId())).collect(Collectors.toList());
        setmealDishService.saveBatch(collect);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("套餐已经上架，不能删除");
        }
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}
