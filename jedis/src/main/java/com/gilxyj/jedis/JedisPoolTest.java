package com.gilxyj.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: redisStudy
 * @description: 在实际应用中，Jedis 实例我们一般都是通过连接池来获取，由于Jedis 对象不是线城安全的，所以，当我们使用Jedis 对象时，从连接池获取Jedis，使用完成之后，再还给连接池。
 * @作者 飞码录
 * @微信公众号 飞码录
 * @网站 http://www.codesboy.cn
 * @国际站 http://www.codesboy.com
 * @微信 gilbertxy
 * @GitHub https://github.com/GilbertXiao
 * @Gitee https://gitee.com/gilbertxiao
 * @create: 2020-05-05 03:07
 **/
public class JedisPoolTest {
    //这段代码采用了jdk1.7的新用法，代码本身上没有问题，但是没有实现强约束。就是有人不会使用jdk7的新用法，甚至finally也不写，导致redis关闭失败。所以需要强约束，让使用redis的人不用考虑资源关闭问题，调用且使用后自动关闭。
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("192.168.133.128");
        try(Jedis jedis = jedisPool.getResource()){
            jedis.auth("p@ssw0rd");
            String ping = jedis.ping();
            System.out.println(ping);
        }
    }
}
