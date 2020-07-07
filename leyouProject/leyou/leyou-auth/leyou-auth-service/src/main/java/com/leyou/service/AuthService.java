package com.leyou.service;

public interface AuthService {
    /**
     * 给用户授权，生成jwt保存到cookie上
     * @param username
     * @param password
     * @return
     */
    String accredit(String username, String password);
}
