package com.imooc.miaosha.redis;

public class MiaoshaKey extends BasePrefix{

	private MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"go");
	public static MiaoshaKey miaoShaPath = new MiaoshaKey(60,"miaoshaPath");
}
