package com.hbr.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hbr.reggie.common.R;
import com.hbr.reggie.pojo.User;
import com.hbr.reggie.service.UserService;
import com.hbr.reggie.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/11 12:27
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    public UserController(UserService userService, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (phone == null) {
            return R.error("手机号不能为空");
        }
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        log.info("手机号：{}，验证码：{}", phone, code);

        // 调用阿里云信息服务发送短信
        //SMSUtils.sendMessage("","","","");

        // 将生成好的验证码放入session中
        //session.setAttribute(phone, code);

        // 将生成好的验证码放入redis中，并设置过期时间为5分钟
        redisTemplate.opsForValue().set(phone, code, 5 * 60, TimeUnit.SECONDS);
        return R.success("发送验证码成功");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");
        //String code = "123456";
        // 从session中获取验证码
        //String sessionCode = (String) session.getAttribute(phone);
        // 从redis中获取验证码
        String sessionCode = redisTemplate.opsForValue().get(phone);

        if (code.equals(sessionCode)) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            // 如果登陆成功，就将redis中的验证码删除
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("验证码错误");
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
