package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbr.reggie.common.CustomException;
import com.hbr.reggie.common.R;
import com.hbr.reggie.dto.DishDto;
import com.hbr.reggie.pojo.Category;
import com.hbr.reggie.pojo.Dish;
import com.hbr.reggie.pojo.DishFlavor;
import com.hbr.reggie.service.CategoryService;
import com.hbr.reggie.service.DishFlavorService;
import com.hbr.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DishController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/10 10:47
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;
    private final DishFlavorService dishFlavorService;
    private final CategoryService categoryService;

    public DishController(DishService dishService, DishFlavorService dishFlavorService, CategoryService categoryService) {
        this.dishService = dishService;
        this.dishFlavorService = dishFlavorService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("保存菜品信息：{}", dishDto.toString());
        dishService.saveWithFlavors(dishDto);
        return R.success("新增菜品成功");
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品信息：{}", dishDto.toString());
        dishService.updateWithFlavors(dishDto);
        return R.success("修改菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name) {
        log.info("分页查询菜品信息：page={},pageSize={}", page, pageSize);
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotBlank(name), Dish::getName, name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, dishLambdaQueryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<DishDto> collect = dishPage.getRecords().stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            // 根据id查询分类
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(collect);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishDtoById(id);
        return R.success(dishDto);
    }

    @PostMapping("/status/{type}")
    public R<String> updateStatus(@PathVariable Integer type, @RequestParam List<String> ids) {
        log.info("修改菜品状态：type={},ids={}", type, ids);
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.in(ids != null && !ids.isEmpty(), Dish::getId, ids);
        dishLambdaUpdateWrapper.set(Dish::getStatus, type);
        dishService.update(dishLambdaUpdateWrapper);
        return R.success("修改菜品状态成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        List<DishDto> collect = list.stream().map(dish1 -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);
            Long categoryId = dishDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            Long id = dishDto.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> list1 = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(collect);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除菜品：ids={}", ids);
        try {
            dishService.delete(ids);
        } catch (CustomException e) {
            return R.error(e.getMessage());
        }
        return R.success("删除菜品成功");
    }
}
