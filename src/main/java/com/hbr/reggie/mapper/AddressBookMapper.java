package com.hbr.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbr.reggie.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName AddressMapper
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 15:32
 * @Version 1.0
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
