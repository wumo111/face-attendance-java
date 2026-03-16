package com.face.controller;

import com.face.common.Result;
import com.face.service.UserService;
import com.face.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        try {
            String token = userService.login(username, password);
            return Result.success(Map.of("token", token));
        } catch (Exception e) {
            return Result.error(401, e.getMessage());
        }
    }

    @PostMapping("/user/register")
    public Result<String> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String role = params.get("role"); // ADMIN or USER

        try {
            userService.register(username, password, role);
            return Result.success("Registered successfully");
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }
}
