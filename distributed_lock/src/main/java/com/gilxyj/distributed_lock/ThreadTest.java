package com.gilxyj.distributed_lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
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
 * @create: 2020-05-05 22:53
 **/
public class ThreadTest extends Thread{

    public final static String CHECK_VALUE_EQUAL_COMMAND="a5680effa13728b76dcee7c24a3d5f04bca1dde3";
    private Redis redis;

    public ThreadTest(String name,Redis redis) {
        super(name);
        this.redis=redis;
    }

    @Override
    public void run() {
        System.out.println(this.getName());
        redis.execute(jedis->{
            String value = UUID.randomUUID().toString();
            SetParams setParams = new SetParams().nx().ex(15);
            String set = jedis.set("k1", value, setParams);
            if (set!=null&&"OK".equalsIgnoreCase(set)) {
                //没人占位
                jedis.set("name" ,"javaboy");
                String name = jedis.get("name");
                System.out.println(this.getName()+name );
                //释放锁
                //jedis.evalsha(CHECK_VALUE_EQUAL_COMMAND, Arrays.asList("k1"), Arrays.asList(value));
            }else{
                //有人占位。。。
                System.out.println(this.getName()+"someone use the lock");
            }
        });
    }
}
