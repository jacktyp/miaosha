package com.imooc.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.redis.RedisService;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate ;

	/**
	 * 用户秒杀商品
	 * @param mm
	 */
	public void sendMiaoshaMessage(MiaoshaMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("秒杀发送消息: "+msg);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}


	
	
}
