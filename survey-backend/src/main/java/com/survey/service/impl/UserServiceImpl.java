package com.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.survey.dto.request.LoginRequest;
import com.survey.dto.request.RegisterRequest;
import com.survey.dto.response.LoginVO;
import com.survey.dto.response.UserVO;
import com.survey.entity.User;
import com.survey.mapper.UserMapper;
import com.survey.service.UserService;
import com.survey.util.JwtUtil;
import com.survey.util.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;

    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginVO login(LoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        if (!PasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setRefreshToken(refreshToken);
        vo.setExpiresIn(jwtUtil.getExpiration());
        vo.setUser(convertToUserVO(user));
        return vo;
    }

    @Override
    public void register(RegisterRequest request) {
        User existUser = getUserByUsername(request.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setRole("user");
        user.setStatus(1);

        save(user);
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserVO(user);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    private UserVO convertToUserVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}