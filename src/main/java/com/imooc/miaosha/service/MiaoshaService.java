package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

/**
 * @ClassName MiaoshaService
 * @Description TODO
 * @Date 2020/5/25 20:16
 * @Created by typ
 */
public interface MiaoshaService {
    /**
     * 秒杀商品
     * @param user      用户
     * @param goods     商品
     * @return
     */
    OrderInfo miaosha(MiaoshaUser user, GoodsVo goods);

    /**
     * 获取秒杀的结果
     * @param uid
     * @param goodsId
     * @return
     */
    long getMiaoshaResult(Long uid, long goodsId);
}
