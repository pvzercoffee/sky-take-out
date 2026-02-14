package com.sky.controller.user;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端用户管理
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
}
