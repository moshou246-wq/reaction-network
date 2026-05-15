package com.reaction.service.impl;

//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reaction.dto.LoginRequest;
import com.reaction.dto.LoginResponse;
import com.reaction.entity.User;
import com.reaction.mapper.UserMapper;
import com.reaction.service.UserService;
import com.reaction.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getEmail());
    }
    
    @Override
    public User register(User user) {
        User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        save(user);
        return user;
    }
    
    @Override
    public User getUserByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }
    
    @Override
    public User getUserInfo(Long userId) {
        return getById(userId);
    }
}
