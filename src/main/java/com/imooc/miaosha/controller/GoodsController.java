package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.service.GoodService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName GoodsController
 * @Description 商品
 * @Date 2020/5/6 8:30
 * @Created by typ
 */
@Controller
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodService goodService;

    /**
     * 查询所有的秒杀商品
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("to_list")
    public String toList(Model model, MiaoshaUser user){
        List<GoodsVo> goodList = goodService.getAllGoods();
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodList);
        return "goods_list";
    }

    /**
     * 商品详情页
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        GoodsVo goods = goodService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        //秒杀状态： 0 未开始， 1 进行中， 2 已经结束
        int miaoshaStatus = 0;
        //秒杀剩余时间
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
