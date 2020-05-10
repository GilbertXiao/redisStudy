package com.gilxyj.redis_delay_queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

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
 * @create: 2020-05-06 23:58
 **/
public class DelayMsgQueue {

    private Redis redis;
    private String queueId;

    public DelayMsgQueue(Redis redis, String queueId) {
        this.redis = redis;
        this.queueId = queueId;
    }

    public void queue(Object data){
        redis.execute(jedis -> {
            JavaboyMessage javaboyMessage = new JavaboyMessage();
            javaboyMessage.setId(UUID.randomUUID().toString());
            javaboyMessage.setObject(data);
            String s = null;
            try {
                s =  new ObjectMapper().writeValueAsString(javaboyMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (s != null) {
                System.out.println("queue"+new Date());
                jedis.zadd(queueId, System.currentTimeMillis() + 5000, s);
            }
        });

    }

    public void loop(){
        redis.execute(jedis -> {
            while (!Thread.interrupted()) {

                Set<String> msgSet = jedis.zrangeByScore(queueId, 0, System.currentTimeMillis(), 0, 1);
                if (msgSet.isEmpty()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    continue;
                }

                String next = msgSet.iterator().next();
                if (jedis.zrem(queueId, next) > 0) {

                    System.out.println("loop"+new Date());
                    //抢到了
                    JavaboyMessage javaboyMessage = null;
                    try {
                        javaboyMessage = new ObjectMapper().readValue(next, JavaboyMessage.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+javaboyMessage);
                }
            }
        });
    }

}
