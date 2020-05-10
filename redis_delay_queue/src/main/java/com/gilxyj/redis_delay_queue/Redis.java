package com.gilxyj.redis_delay_queue;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: redisStudy
 * @description: 实现 调用redis后关闭资源 的强约束
 * @作者 飞码录
 * @微信公众号 飞码录
 * @网站 http://www.codesboy.cn
 * @国际站 http://www.codesboy.com
 * @微信 gilbertxy
 * @GitHub https://github.com/GilbertXiao
 * @Gitee https://gitee.com/gilbertxiao
 * @create: 2020-05-05 03:43
 **/
public class Redis {

    public Redis(){
    }

    private static volatile JedisPool jedisPool = null;

    private static JedisPool getInstance(){
        if (jedisPool == null) {
            synchronized (Redis.class){
                if (jedisPool == null) {
                    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                    //最大空闲数
                    poolConfig.setMaxIdle(300);
                    //最大连接数
                    poolConfig.setMaxTotal(1000);
                    //最大等待时间，-1表示没有限制
                    poolConfig.setMaxWaitMillis(30000);
                    //在空闲时检查有效性
                    poolConfig.setTestOnBorrow(true);

                    jedisPool = new JedisPool(poolConfig, "192.168.133.128", 6379, 30000, "p@ssw0rd");
                }
            }
        }
        return jedisPool;
    }

    public void execute(CallWithJedis callWithJedis){
        try(Jedis resource = getInstance().getResource()){
           // System.out.println(getInstance());
            callWithJedis.call(resource);
        }
    }


}
