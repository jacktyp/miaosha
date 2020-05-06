package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName GoodsController
 * @Description TODO
 * @Date 2020/5/6 8:30
 * @Created by typ
 */
@Controller
@RequestMapping("goods")
public class GoodsController {

    @RequestMapping("to_list")
    public String toList(Model model, MiaoshaUser user){
        model.addAttribute("user",user);
        return "goods_list";
    }
}
