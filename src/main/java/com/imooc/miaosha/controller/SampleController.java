package com.imooc.miaosha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName SampleController
 * @Description TODO
 * @Date 2020/5/4 15:25
 * @Created by typ
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @RequestMapping("thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("user"," typ");
        return "hello";
    }



}
