package com.imooc.miaosha.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * (OrderInfo)实体类
 *
 * @author makejava
 * @since 2020-05-25 18:43:07
 */
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = -27685124139266082L;
    /**
    * 订单ID
    */
    private Long id;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 收货地址ID
    */
    private Long goodsAddrId;
    /**
    * 冗余过来的商品名称
    */
    private String goodsName;
    /**
    * 商品数量
    */
    private Integer goodsCount;
    /**
    * 商品单价
    */
    private Double goodsPrice;
    /**
    * 1-pc,2android,3-ios
    */
    private Object orderChannel;
    /**
    * 订单状态，0未支付，1已支付，2已发货，3已收货，4已退款，5已完成
    */
    private Object status;
    /**
    * 订单创建时间
    */
    private Date createDate;
    /**
    * 支付时间
    */
    private Date payDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getGoodsAddrId() {
        return goodsAddrId;
    }

    public void setGoodsAddrId(Long goodsAddrId) {
        this.goodsAddrId = goodsAddrId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Object getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(Object orderChannel) {
        this.orderChannel = orderChannel;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

}