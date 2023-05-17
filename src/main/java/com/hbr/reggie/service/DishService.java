package com.hbr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbr.reggie.dto.DishDto;
import com.hbr.reggie.pojo.Dish;

import java.util.List;

/**
 * @ClassName DishService
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 18:26
 * @Version 1.0
 */
public interface DishService extends IService<Dish> {
    void saveWithFlavors(DishDto dishDto);

    DishDto getDishDtoById(Long id);

    void updateWithFlavors(DishDto dishDto);

    void updateStatus(Integer type, List<String> ids);

    void delete(List<Long> ids);
}
