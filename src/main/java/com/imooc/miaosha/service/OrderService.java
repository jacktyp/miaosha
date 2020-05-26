package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

/**
 * @ClassName OrderService
 * @Description TODO
 * @Date 2020/5/25 20:10
 * @Created by typ
 */
public interface OrderService {
    /**
     * 判断用户是否秒杀过当前商品
     * @param id        用户ID
     * @param goodsId   商品ID
     * @return
     */
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, long goodsId);

    /**
     * 创建秒杀订单
     * @param user
     * @param goods
     * @return
     */
    OrderInfo createOrder(MiaoshaUser user, GoodsVo goods);
}
