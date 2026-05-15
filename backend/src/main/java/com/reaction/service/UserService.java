package com.reaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reaction.entity.User;
import com.reaction.dto.LoginRequest;
import com.reaction.dto.LoginResponse;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 用户注册
     */
    User register(User user);
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    
    /**
     * 获取用户信息
     */
    User getUserInfo(Long userId);
}
