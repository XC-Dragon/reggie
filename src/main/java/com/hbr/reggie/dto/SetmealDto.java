package com.hbr.reggie.dto;

import com.hbr.reggie.pojo.Setmeal;
import com.hbr.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
