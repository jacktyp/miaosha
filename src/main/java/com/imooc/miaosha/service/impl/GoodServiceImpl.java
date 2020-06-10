package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.service.GoodService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName GoodServiceImpl
 * @Description TODO
 * @Date 2020/5/25 18:50
 * @Created by typ
 */
@Service
public class GoodServiceImpl implements GoodService {
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsVo> getAllGoods() {
        return goodsDao.getAllGoods();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 减库存 -1
     * @param goods
     * @return
     */
    @Override
    @Transactional
    public Boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g.getGoodsId());
        return ret > 0;
    }
}
