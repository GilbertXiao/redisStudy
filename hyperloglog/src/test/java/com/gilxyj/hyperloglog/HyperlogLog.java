package com.gilxyj.hyperloglog;

import org.junit.Test;
import redis.clients.jedis.util.RedisInputStream;

/**
 * @program: redisStudy
 * @description:
 * @作者 飞码录
 * @微信公众号 飞码录
 * @网站 http://www.codesboy.cn
 * @国际站 http://www.codesboy.com
 * @微信 gilbertxy
 * @GitHub https://github.com/GilbertXiao
 * @Gitee https://gitee.com/gilbertxiao
 * @create: 2020-05-08 17:30
 **/
public class HyperlogLog {

    @Test
    public void test01(){
        Redis redis = new Redis();
        redis.execute(jedis -> {
            for (int i = 0; i < 10000; i++) {
                jedis.pfadd("uv", "u" + i);
                long uv = jedis.pfcount("uv");
                if (uv != Long.valueOf(i+1)) {
                    System.out.println("-------------------");
                    System.out.println(String.valueOf(uv));
                    System.out.println(String.valueOf(i+1));
                }

            }
        });
    }
}
