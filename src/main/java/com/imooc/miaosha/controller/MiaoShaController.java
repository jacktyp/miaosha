package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import com.imooc.miaosha.vo.MiaoShaResultVo;
import com.imooc.miaosha.vo.OrderDetailVo;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName MiaoShaController
 * @Description TODO
 * @Date 2020/5/25 20:07
 * @Created by typ
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {

    @Autowired
    RedisService redisService;

    @Autowired
    GoodService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQSender sender;

    //判断库存是否为空
    Map<Long,Boolean> localMap = new ConcurrentHashMap<>();

    /**
     * 系统初始化bean时执行该方法
     * 将商品库存加载到redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> allGoods = goodsService.getAllGoods();
        if (CollectionUtils.isEmpty(allGoods)){
            return;
        }
        allGoods.forEach(good->{
            redisService.set(GoodsKey.getMiaoshaGoodsStock,String.valueOf(good.getId()),good.getStockCount());
            localMap.put(good.getId(),false);
        });
    }


    /**
     * 秒杀商品
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_miaosha2", method= RequestMethod.POST)
    public String do_miaosha2(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg","商品已经秒杀完毕");
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", "不能重复秒杀");
            return "miaosha_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

    /**
     *ajax调用
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_miaosha1", method= RequestMethod.POST)
    @ResponseBody
    public Result<OrderDetailVo> do_miaosha1(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        //返回数值
        orderDetailVo.setGoods(goods);
        orderDetailVo.setOrder(orderInfo);
        return Result.success(orderDetailVo);
    }

    /**
     * 使用消息队列
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_miaosha", method= RequestMethod.POST)
    @ResponseBody
    public Result<Integer> do_miaosha(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //如果时true，库存不足
        Boolean over = localMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //减库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, String.valueOf(goodsId));
        //返回库存，如果不足，返回秒杀结束
        if (stock < 0 ){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        //排队中 0表示排队
        return Result.success(0);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
    }

}
