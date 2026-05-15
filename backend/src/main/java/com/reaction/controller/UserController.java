package com.reaction.controller;

import com.reaction.dto.ApiResponse;
import com.reaction.dto.LoginRequest;
import com.reaction.dto.LoginResponse;
import com.reaction.entity.User;
import com.reaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ApiResponse.success("Login successful", response);
        } catch (Exception e) {
            return ApiResponse.error("Login failed: " + e.getMessage());
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        try {
            User newUser = userService.register(user);
            return ApiResponse.success("Registration successful", newUser);
        } catch (Exception e) {
            return ApiResponse.error("Registration failed: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public ApiResponse<User> getUserInfo(@PathVariable Long userId) {
        try {
            User user = userService.getUserInfo(userId);
            if (user != null) {
                return ApiResponse.success("Get user info successful", user);
            }
            return ApiResponse.error("User not found");
        } catch (Exception e) {
            return ApiResponse.error("Get user info failed: " + e.getMessage());
        }
    }
}
