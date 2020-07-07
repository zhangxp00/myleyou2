package com.leyou.controller;

import com.leyou.pojo.User;
import com.leyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    /**
     * 校验数据是否可用
     *
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        Boolean bool = userServiceImpl.checkUser(data, type);
        if (bool == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }

    /**
     * 根据手机号发送短信
     *
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendSms(@RequestParam(value = "phone") String phone) {
        userServiceImpl.sendSms(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户注册
     *
     * @param user
     * @param Code
     * @return
     * @Valid 标记何时进行校验
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid User user, @RequestParam("code") String Code) {
        userServiceImpl.registerUser(user, Code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> findUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userServiceImpl.findUser(username, password);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
