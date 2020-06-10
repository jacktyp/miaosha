package com.imooc.miaosha.redis;

public class GoodsKey extends BasePrefix{

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	//商品列表页面
	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
	//商品详情页面
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
	//秒杀商品库存
	public static GoodsKey getMiaoshaGoodsStock= new GoodsKey(0, "gs");
}
