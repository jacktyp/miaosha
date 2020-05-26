package com.imooc.miaosha.service;

import com.imooc.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * @ClassName GoodService
 * @Description TODO
 * @Date 2020/5/25 18:49
 * @Created by typ
 */
public interface GoodService {
    /**
     * 查询所有的秒杀商品
     * @return
     */
    List<GoodsVo> getAllGoods();

    /**
     * 获取商品详细信息
     * @param goodsId
     * @return
     */
    GoodsVo getGoodsVoByGoodsId(long goodsId);

    /**
     * 秒杀减库存
     * @param goods
     */
    void reduceStock(GoodsVo goods);
}
