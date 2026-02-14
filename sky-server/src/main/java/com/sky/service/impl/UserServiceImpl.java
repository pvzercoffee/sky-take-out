package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    WeChatProperties weChatProperties;

    @Autowired
    UserMapper userMapper;

    /**
     * 用户微信登陆
     * @param loginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO loginDTO) {

        //调用微信接口服务，获得微信openid
        String openid = getOpenid(loginDTO.getCode());

        //判断openid是否为空，若为空则登陆失败
        if(openid == null || openid == ""){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否为新用户
        User user = userMapper.getByOpenid(openid);

        //是，自动完成注册
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回用户对象

        return user;
    }

    //调用微信接口服务，获得微信openid
    private String getOpenid(String jsCode){
        Map<String,String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",jsCode);
        map.put("grant_type","authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN,map);

        JSONObject jsonObject = JSONObject.parseObject(json);

        return jsonObject.getString("openid");
    }
}
