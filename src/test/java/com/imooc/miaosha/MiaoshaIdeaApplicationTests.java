package com.imooc.miaosha;

import com.imooc.miaosha.redis.BasePrefix;
import com.imooc.miaosha.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiaoshaIdeaApplicationTests {

	@Autowired
	RedisService redisService;

	@Test
	void contextLoads() {
		//redisService.set(,"111","haha");
	}

}
