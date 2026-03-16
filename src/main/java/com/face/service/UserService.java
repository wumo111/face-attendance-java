package com.face.service;

import com.face.entity.User;
import com.face.mapper.UserMapper;
import com.face.utils.JwtUtils;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !user.getPassword().equals(SecureUtil.md5(password))) {
            throw new RuntimeException("Invalid username or password");
        }
        return jwtUtils.generateToken(username, user.getRole());
    }

    public void register(String username, String password, String role) {
        if (userMapper.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(SecureUtil.md5(password));
        user.setRole(role);
        userMapper.insert(user);
    }
}
