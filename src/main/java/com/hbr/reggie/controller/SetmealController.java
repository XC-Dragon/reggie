package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbr.reggie.common.CustomException;
import com.hbr.reggie.common.R;
import com.hbr.reggie.dto.SetmealDto;
import com.hbr.reggie.pojo.Category;
import com.hbr.reggie.pojo.Setmeal;
import com.hbr.reggie.pojo.SetmealDish;
import com.hbr.reggie.service.CategoryService;
import com.hbr.reggie.service.SetmealDishService;
import com.hbr.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SetmealController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/10 22:51
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private final CategoryService categoryService;
    private final SetmealService setmealService;
    private final SetmealDishService setmealDishService;

    public SetmealController(CategoryService categoryService, SetmealService setmealService, SetmealDishService setmealDishService) {
        this.categoryService = categoryService;
        this.setmealService = setmealService;
        this.setmealDishService = setmealDishService;
    }

    @PostMapping
    @CacheEvict(value = "setmeal", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("保存套餐信息：{}", setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/{id}")
    @Cacheable(value = "setmeal", key = "#id")
    public R<SetmealDto> getById(@PathVariable String id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        Category category = categoryService.getById(setmeal.getCategoryId());
        setmealDto.setCategoryName(category.getName());
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDto.setSetmealDishes(setmealDishService.list(setmealDishLambdaQueryWrapper));
        return R.success(setmealDto);
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotBlank(name), Setmeal::getName, name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> setmealPageRecords = setmealPage.getRecords();
        List<SetmealDto> collect = setmealPageRecords.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(collect);

        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    @CacheEvict(value = "setmeal", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除套餐信息：{}", ids.toString());
        try {
            setmealService.removeWithDish(ids);
        } catch (CustomException e) {
            return R.error(e.getMessage());
        }
        return R.success("删除套餐成功");
    }


    @PostMapping("/status/{type}")
    public R<String> updateStatus(@PathVariable Integer type, @RequestParam List<Long> ids) {
        log.info("更新套餐状态：{}", ids.toString());
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        List<Setmeal> collect = list.stream().peek(setmeal -> setmeal.setStatus(type)).collect(Collectors.toList());
        setmealService.updateBatchById(collect);
        return R.success("更新套餐状态成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmeal", key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setmealLambdaUpdateWrapper.eq(Setmeal::getId, setmealDto.getId());
        setmealService.updateById(setmealDto);

        // 获取套餐内菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        // 删除原记录
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        // 绑定套餐id
        List<SetmealDish> collect = setmealDishes.stream().peek(setmealDish -> setmealDish.setSetmealId(setmealDto.getId())).collect(Collectors.toList());
        // 插入
        setmealDishService.saveBatch(collect);
        return R.success("更新成功！");
    }
}
