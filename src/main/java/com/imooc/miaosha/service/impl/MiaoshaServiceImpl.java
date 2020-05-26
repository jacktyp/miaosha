package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.MiaoShaDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.service.GoodService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
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
    private MiaoShaDao miaoShaDao;
    @Autowired
    private GoodService goodService;
    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //商品库存减一
        goodService.reduceStock(goods);
        //创建订单
        OrderInfo orderInfo = orderService.createOrder(user,goods);
        return orderInfo;
    }
}
