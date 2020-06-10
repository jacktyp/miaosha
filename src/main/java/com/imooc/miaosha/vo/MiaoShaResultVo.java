package com.imooc.miaosha.vo;

/**
 * @ClassName MiaoShaResultVo
 * @Description 秒杀结果信息封装
 * @Date 2020/6/11 7:32
 * @Created by typ
 */
public class MiaoShaResultVo {
    private Long resultNum;
    private Long orderId;

    public Long getResultNum() {
        return resultNum;
    }

    public void setResultNum(Long resultNum) {
        this.resultNum = resultNum;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
