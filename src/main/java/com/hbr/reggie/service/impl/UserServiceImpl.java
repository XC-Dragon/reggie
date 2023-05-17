package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.mapper.UserMapper;
import com.hbr.reggie.pojo.User;
import com.hbr.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 12:30
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
