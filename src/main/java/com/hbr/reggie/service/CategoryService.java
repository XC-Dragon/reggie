package com.hbr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbr.reggie.pojo.Category;

/**
 * @ClassName CategoryService
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 17:47
 * @Version 1.0
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
