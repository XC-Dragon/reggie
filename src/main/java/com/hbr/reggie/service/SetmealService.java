package com.hbr.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbr.reggie.dto.SetmealDto;
import com.hbr.reggie.pojo.Setmeal;

import java.util.List;

/**
 * @ClassName Setmeal
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 18:28
 * @Version 1.0
 */
public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
