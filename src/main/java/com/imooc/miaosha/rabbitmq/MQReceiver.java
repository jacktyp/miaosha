package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.service.GoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;
		
		@Autowired
		GoodService goodsService;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		MiaoshaService miaoshaService;

		/**
		 * 监听秒杀队列--
		 * @param message
		 */
		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
			MiaoshaUser user = mm.getUser();
			long goodsId = mm.getGoodsId();
			
			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
	    	int stock = goods.getStockCount();
	    	if(stock <= 0) {
	    		return;
	    	}
	    	/*//判断是否已经秒杀到了
	    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
	    	if(order != null) {
	    		return;
	    	}*/
	    	//减库存 下订单 写入秒杀订单
	    	miaoshaService.miaosha(user, goods);
		}

}
