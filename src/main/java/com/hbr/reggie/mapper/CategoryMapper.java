package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName CategoryMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 17:46
 * @Version 1.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
