package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @ClassName OrderDao
 * @Description TODO
 * @Date 2020/5/25 20:12
 * @Created by typ
 */
@Mapper
public interface OrderDao {
    /**
     *判断用户是否秒杀过当前商品
     * @param userId    用户ID
     * @param goodsId   商品ID
     * @return
     */
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    /**
     * 订单创建
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);

    /**
     * 秒杀订单创建
     * @param miaoshaOrder
     * @return
     */
    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId")long orderId);
}
