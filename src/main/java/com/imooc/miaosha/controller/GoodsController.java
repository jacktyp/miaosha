package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;



    /**
     * 查询所有的秒杀商品
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "to_list",produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user){
        //返回用户信息
        model.addAttribute("user",user);
        //如果有缓存，先取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //没有就查询数据库，并存入缓存
        List<GoodsVo> goodList = goodService.getAllGoods();
        model.addAttribute("goodsList",goodList);
        //手动渲染页面
        html = getThymeleafHtml(request,response,model,"goods_list");
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    /**
     * 直接插数据库
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "to_list1")
    public String toList1(Model model, MiaoshaUser user){
        //返回用户信息
        model.addAttribute("user",user);
        List<GoodsVo> goodList = goodService.getAllGoods();
        model.addAttribute("goodsList",goodList);
        return "goods_list";
    }

    /**
     * ajax 实现页面静态化
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(MiaoshaUser user,@PathVariable("goodsId")long goodsId) {
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        GoodsVo goods = goodService.getGoodsVoByGoodsId(goodsId);

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
        //需要返回的值
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }

    /**
     * 商品详情页  动态页面，页面缓存-手动渲染
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response,Model model,MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId) {
        //返回用户信息
        model.addAttribute("user", user);
        //如果有缓存，先取缓存--key是商品ID
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
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
        //手动渲染页面
        html = getThymeleafHtml(request,response,model,"goods_detail");
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        //return "goods_detail";
        return html;
    }

    /**
     * 手动渲染thymeleaf
     * @param request
     * @param response
     * @param model
     * @param htmlPage  需要渲染的页面
     * @return
     */
    public String getThymeleafHtml(HttpServletRequest request, HttpServletResponse response,
                                   Model model,String htmlPage){
        IWebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        return thymeleafViewResolver.getTemplateEngine().process(htmlPage, ctx);
    }
}
