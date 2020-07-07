package com.leyou.cartFilter;

import com.leyou.cartConfig.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EnableConfigurationProperties(JwtProperties.class)
@Component
public class CartInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private static ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        //解析token
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        //将解析后的对象放入到线程的局部变量中
        if (userInfo == null) {
            return false;
        }
        THREAD_LOCAL.set(userInfo);
        return true;
    }

    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空线程的局部变量
        THREAD_LOCAL.remove();
    }
}
