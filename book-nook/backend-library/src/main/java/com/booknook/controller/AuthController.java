package com.booknook.controller;

import com.booknook.common.Result;
import com.booknook.dto.LoginDTO;
import com.booknook.dto.LoginVO;
import com.booknook.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 登录接口。 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        return Result.ok(authService.login(dto));
    }
}
