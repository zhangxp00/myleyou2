package com.leyou.controller;

import com.leyou.ConfigParam.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.service.AuthService;
import com.leyou.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authServiceImpl;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 给用户授权，生成jwt保存到cookie上,完成登录
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("accredit")
    private ResponseEntity<Void> accredit(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = authServiceImpl.accredit(username, password);
        if (token == null) {
            //身份认证不通过  401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getExpire() * 60);
        return ResponseEntity.ok(null);
    }

    /**
     * 登陆成功后，获取对应的登录者信息
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            if (userInfo == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //登录成功以后重新设置jwt以及cookie实现刷新有效时间的功能
            token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getExpire() * 60);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
