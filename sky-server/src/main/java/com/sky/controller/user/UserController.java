package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户端用户管理
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProperties jwtProperties;

    /**
     * 微信登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登陆")
    public Result<UserLoginVO> login(@RequestBody  UserLoginDTO loginDTO){

        log.info("微信用户登录："+loginDTO);
        User user = userService.login(loginDTO);

        //为微信用户生成jwt令牌

        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());

        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);

        UserLoginVO loginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .openid(user.getOpenid())
                .build();

        return Result.success(loginVO);
    }
}
