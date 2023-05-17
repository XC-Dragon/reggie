package com.hbr.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hbr.reggie.common.BaseContext;
import com.hbr.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginFilter
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/8 22:52
 * @Version 1.0
 */

@Slf4j
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        // 1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("本次请求的URI为：{}", requestURI);
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/upload/**",
                "/user/sendMsg",
                "/user/login"
        };
        // 2、判断本次请求的URI是否是登录请求，如果是，直接放行
        if (isMatch(urls, requestURI)) {
            log.info("本次请求的URI为登录请求，直接放行");
            filterChain.doFilter(request, response);
            return;
        }
        // 3.1、判断登陆状态，如果已经登录，直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("本次请求的URI为非登录请求，已经登录，用户ID：{}，直接放行", request.getSession().getAttribute("employee"));

            Long employeeId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setUserId(employeeId);

            log.info("设置用户ID：{}", employeeId);
            filterChain.doFilter(request, response);
            return;
        }
        // 3.2、判断登陆状态，如果已经登录，直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("本次请求的URI为非登录请求，已经登录，用户ID：{}，直接放行", request.getSession().getAttribute("user"));

            Long employeeId = (Long) request.getSession().getAttribute("user");
            BaseContext.setUserId(employeeId);

            log.info("设置用户ID：{}", employeeId);
            filterChain.doFilter(request, response);
            return;
        }
        // 4、如果没有登录，跳转到登录页面
        log.info("本次请求的URI为非登录请求，未登录，跳转到登录页面");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean isMatch(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
