package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserService
 * @Description TODO
 * @Date 2020/5/6 6:59
 * @Created by typ
 */
public interface UserService {
    /**
     * 登陆操作
     * @param response
     * @param loginVo   参数
     */
    void doLogin(HttpServletResponse response, LoginVo loginVo);

    /**
     * 根据token获取redis中用户，并刷新
     * @param response
     * @param token
     * @return
     */
    MiaoshaUser getByToken(HttpServletResponse response, String token);
}
