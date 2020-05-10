package com.gilxyj.jedis;

import redis.clients.jedis.Jedis;

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
 * @create: 2020-05-05 03:00
 **/
public class MyJedis {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.133.128");
        jedis.auth("p@ssw0rd");
        String ping = jedis.ping();
        System.out.println(ping);


    }
}
