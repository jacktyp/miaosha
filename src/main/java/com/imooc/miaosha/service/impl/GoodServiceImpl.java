package com.imooc.miaosha.service.impl;

import com.imooc.miaosha.dao.GoodsDao;
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

    @Override
    @Transactional
    public void reduceStock(GoodsVo goods) {
        //商品库存减一
        goodsDao.reduceStock(goods.getId());
    }
}
