package com.hbr.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbr.reggie.mapper.ShoppingCartMapper;
import com.hbr.reggie.pojo.ShoppingCart;
import com.hbr.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @ClassName ShoppingCartServiceImpl
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 22:46
 * @Version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
