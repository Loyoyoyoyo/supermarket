package com.lolo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @ResponseBody
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截请求{}",request.getServletPath());
        if(request.getCookies() == null){
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().write("请先登录！");
            return false;
        }
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie:cookies) {
            if(cookie.getName().equals("login")){
                return true;
            }
        }
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("请先登录！");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
