package com.imooc.miaosha.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * (MiaoshaGoods)实体类
 *
 * @author makejava
 * @since 2020-05-25 18:43:04
 */
public class MiaoshaGoods implements Serializable {
    private static final long serialVersionUID = -47284469891602921L;
    /**
    * 秒杀商品ID
    */
    private Long id;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 秒杀价
    */
    private Double miaoshaPrice;
    /**
    * 商品库存
    */
    private Integer stockCount;
    /**
    * 秒杀开始时间
    */
    private Date startDate;
    /**
    * 秒杀结束时间
    */
    private Date endDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}