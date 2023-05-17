package com.hbr.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName ReggieApplication
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/8 19:04
 * @Version 1.0
 */
@Slf4j
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
    }
}


