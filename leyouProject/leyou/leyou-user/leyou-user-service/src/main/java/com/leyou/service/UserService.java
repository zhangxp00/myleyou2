package com.leyou.service;

import com.leyou.pojo.User;

public interface UserService {
    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    Boolean checkUser(String data, Integer type);

    /**
     * 发送短信
     * @param phone
     */
    void sendSms(String phone);

    /**
     * 用户注册
     * @param user
     * @param code
     */
    void registerUser(User user, String code);

    /**
     * 根据用户名密码查询用户
     * @param username
     * @param password
     * @return
     */
    User findUser(String username, String password);
}
