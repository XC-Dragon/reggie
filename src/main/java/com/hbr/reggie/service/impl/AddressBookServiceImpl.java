package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.mapper.AddressBookMapper;
import com.hbr.reggie.pojo.AddressBook;
import com.hbr.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @ClassName AddressBookServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 15:34
 * @Version 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
