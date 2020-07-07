package com.leyou.api;

import com.leyou.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    /**
     * 根据用户名密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public User findUser(@RequestParam("username") String username, @RequestParam("password") String password);
}
