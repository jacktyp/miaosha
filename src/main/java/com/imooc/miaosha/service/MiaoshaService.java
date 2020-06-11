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

    /**
     * 生成秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createMiaoShaPath(MiaoshaUser user, long goodsId);

    /**
     * 验证秒杀地址
     * @param path
     * @return
     */
    boolean checkMiaoShaPath(String path,Long userId,Long goodsId);
}
