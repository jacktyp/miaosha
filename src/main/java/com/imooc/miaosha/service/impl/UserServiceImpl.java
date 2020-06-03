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
    public String doLogin(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //根据ID--手机号，查询用户
        MiaoshaUser user = getById(Long.parseLong(mobile));
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
        /*MiaoshaUser user = new MiaoshaUser();
        user.setId(Long.parseLong(mobile));
        user.setPassword(password);*/
        //生成cookies
        String token = UUIDUtil.uuid();
        addCookies(user,response,token);
        return token;
    }

    /**
     * 通过用户ID获取用户信息
     * @param userId    用户ID
     * @return
     */
    public MiaoshaUser getById(Long userId){
        //现存数据库中找user
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+userId, MiaoshaUser.class);
        if (user != null){
            return user;
        }
        //如果没有就去数据库
        user = userDao.getById(userId);
        if (user != null){
            redisService.set(MiaoshaUserKey.getById, ""+userId, user);
        }
        return user;
    }

    public boolean updatePassword(String token, long userId, String formPass){
        //获取用户
        MiaoshaUser user = getById(userId);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库密码
        String passwd = MD5Util.formPassToDBPass(formPass, user.getSalt());
        userDao.updatePassword(userId,passwd);
        //更新删除缓存用户数据---用户token redis---用户信息
        //删除用户token redis
        redisService.delete(MiaoshaUserKey.getById, ""+userId);
        //更新用户信息
        user.setPassword(passwd);
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
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
