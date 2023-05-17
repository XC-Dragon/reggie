package com.hbr.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName MyMataObjectHandle
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 17:00
 * @Version 1.0
 */
@Slf4j
@Component
public class MyMetaObjectHandle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = BaseContext.getUserId();
        LocalDateTime now = LocalDateTime.now();
        log.info("[插入填充]:userId = {}, now = {}", userId, now);
        metaObject.setValue("createTime", now);
        metaObject.setValue("updateTime", now);
        metaObject.setValue("createUser", userId);
        metaObject.setValue("updateUser", userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getUserId();
        log.info("[更新填充]:userId = {}, now = {}", userId, now);
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", userId);
    }
}
