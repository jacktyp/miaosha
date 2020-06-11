package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.MiaoShaDao;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.GoodsVo;
import com.imooc.miaosha.vo.MiaoShaResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName MiaoshaServiceImpl
 * @Description TODO
 * @Date 2020/5/25 20:16
 * @Created by typ
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {


    @Autowired
    private GoodService goodService;
    @Autowired
    private OrderService orderService;
    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodService.reduceStock(goods);
        if(success) {
            //order_info maiosha_order
            return orderService.createOrder(user, goods);
        }else {
            //设置商品没有了
            setGoodsOver(goods.getId());
            return null;
        }
    }

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if(order != null) {//秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    @Override
    public String createMiaoShaPath(MiaoshaUser user, long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid() + "path");
        redisService.set(MiaoshaKey.miaoShaPath,user.getId()+"_"+goodsId,path);
        return path;
    }

    @Override
    public boolean checkMiaoShaPath(String path,Long userId,Long goodsId) {
        String redisPath = redisService.get(MiaoshaKey.miaoShaPath, userId + "_" + goodsId,String.class);
        return path.equals(redisPath);
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }
}
