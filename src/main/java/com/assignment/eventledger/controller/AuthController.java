package com.assignment.eventledger.controller;

import com.assignment.eventledger.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestParam String username) {
        return JwtUtil.generateToken(username);
    }

}
