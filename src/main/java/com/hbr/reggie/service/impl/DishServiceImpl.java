package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.common.CustomException;
import com.hbr.reggie.dto.DishDto;
import com.hbr.reggie.mapper.DishMapper;
import com.hbr.reggie.pojo.Dish;
import com.hbr.reggie.pojo.DishFlavor;
import com.hbr.reggie.service.DishFlavorService;
import com.hbr.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DishServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 18:27
 * @Version 1.0
 */

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavors(DishDto dishDto) {
        this.save(dishDto);
        Long dishDtoId = dishDto.getId();
        List<DishFlavor> collect = dishDto.getFlavors().stream().peek(flavor -> flavor.setDishId(dishDtoId)).collect(Collectors.toList());

        dishFlavorService.saveBatch(collect);
    }

    @Override
    public DishDto getDishDtoById(Long id) {
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void updateWithFlavors(DishDto dishDto) {
        this.updateById(dishDto);

        Long dishDtoId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDtoId);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> collect = dishDto.getFlavors().stream().peek(flavor -> flavor.setDishId(dishDtoId)).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);
    }

    @Override
    public void updateStatus(Integer type, List<String> ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId, ids);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        List<Dish> collect = dishList.stream().peek(dish -> dish.setStatus(type)).collect(Collectors.toList());
        dishService.updateBatchById(collect);
    }

    @Override
    public void delete(List<Long> ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId, ids);
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("菜品已启售，不能删除！");
        }
        dishService.removeByIds(ids);
    }
}
