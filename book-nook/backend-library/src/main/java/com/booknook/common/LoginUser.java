package com.booknook.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 登录态用户信息。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private Long userId;
    private String username;
    private String role;
    private Long refId;
}
