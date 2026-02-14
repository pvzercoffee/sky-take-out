package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

public interface UserService {
    /**
     * 用户微信登陆
     * @param loginDTO
     * @return
     */
    User login(UserLoginDTO loginDTO);
}
