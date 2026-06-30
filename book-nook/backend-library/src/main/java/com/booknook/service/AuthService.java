package com.booknook.service;

import com.booknook.common.LoginUser;
import com.booknook.dto.LoginDTO;
import com.booknook.dto.LoginVO;
import com.booknook.exception.BusinessException;
import com.booknook.mapper.LibraryMapper;
import com.booknook.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/** 登录服务。 */
@Service
public class AuthService {
    private final LibraryMapper mapper;
    private final JwtUtil jwtUtil;

    public AuthService(LibraryMapper mapper, JwtUtil jwtUtil) {
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    public LoginVO login(LoginDTO dto) {
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            throw new BusinessException("请输入用户名和密码");
        }
        Map<String, Object> user = mapper.findUserByUsername(dto.getUsername());
        if (user == null || !dto.getPassword().equals(String.valueOf(user.get("password")))) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (Number.class.isAssignableFrom(user.get("status").getClass())
                && ((Number) user.get("status")).intValue() != 1) {
            throw new BusinessException(403, "账号已停用");
        }
        Long userId = ((Number) user.get("id")).longValue();
        String role = String.valueOf(user.get("role"));
        Object refIdValue = user.get("ref_id");
        Long refId = refIdValue == null ? null : ((Number) refIdValue).longValue();
        LoginUser loginUser = new LoginUser(userId, dto.getUsername(), role, refId);
        String token = jwtUtil.createToken(loginUser);
        return new LoginVO(token, dto.getUsername(), role, refId);
    }
}
