package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbr.reggie.common.R;
import com.hbr.reggie.pojo.Category;
import com.hbr.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CategoryController
 * @Description 分类管理
 * @Author Hbr
 * @Date 2023/5/9 17:50
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping
    public R<String> save(@RequestBody Category category) {
        log.info("保存分类信息：{}", category.toString());
        categoryService.save(category);
        return R.success("保存成功");
    }

    @RequestMapping("/page")
    public R<Page<Category>> page(Integer page, Integer pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage, queryWrapper);
        return R.success(categoryPage);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类，id为{}", ids);
        categoryService.remove(ids);
        return R.success("分录删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("更新分类信息：{}", category.toString());
        categoryService.updateById(category);
        return R.success("更新成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
