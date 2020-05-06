package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Date 2020/5/6 7:01
 * @Created by typ
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;
    /**
     * 登陆操作
     * @param response
     * @param loginVo   参数
     */
    @Override
    public void doLogin(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //根据ID--手机号，查询用户
        MiaoshaUser user = userDao.getById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPassword = user.getPassword();
        String dbSalt = user.getSalt();
        //将前端密码通过salt加密
        String calcPassword = MD5Util.formPassToDBPass(password, dbSalt);
        if (!calcPassword.equals(dbPassword)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookies
        String token = UUIDUtil.uuid();
        addCookies(user,response,token);
    }

    /**
     * 根据token获取redis中用户，并刷新
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(user != null) {
            addCookies(user,response,token);
        }
        return user;
    }

    public void addCookies(MiaoshaUser user,HttpServletResponse response,String token){
        //存入redis
        redisService.set(MiaoshaUserKey.token,token,user);
        //设置cookies
        Cookie cookie = new Cookie("token",token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
