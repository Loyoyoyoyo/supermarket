package com.lolo.supermarket.interceptor;
import com.lolo.supermarket.entity.User;
import com.lolo.supermarket.util.Result;
import com.lolo.supermarket.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Component
@Slf4j

public class LoginInterceptor implements HandlerInterceptor {
    @ResponseBody
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截请求{}", request.getServletPath());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        } else {
            Result result = ResultGenerator.fail(000,"未登录");
            //
            ObjectMapper mapper = new ObjectMapper();
            String jsonObjectStr = mapper.writeValueAsString(result);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().write(jsonObjectStr);
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
