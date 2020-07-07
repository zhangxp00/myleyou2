package com.leyou.service.Impl;

import com.leyou.ConfigParam.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.feign.UserFeign;
import com.leyou.pojo.User;
import com.leyou.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 给用户授权，生成jwt保存到cookie上,完成登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String accredit(String username, String password) {
        //查询user对象
        User user = userFeign.findUser(username, password);
        //判断是否为空
        if (user == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        //生成jwt类形的token,返回
        String token = null;
        try {
            token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}
