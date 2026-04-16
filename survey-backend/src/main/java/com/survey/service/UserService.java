package com.survey.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.survey.dto.request.LoginRequest;
import com.survey.dto.request.RegisterRequest;
import com.survey.dto.response.LoginVO;
import com.survey.dto.response.UserVO;
import com.survey.entity.User;

public interface UserService extends IService<User> {
    LoginVO login(LoginRequest request);
    void register(RegisterRequest request);
    UserVO getUserInfo(Long userId);
    User getUserByUsername(String username);
}
