package com.gilxyj.jedis.stronglimit;

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
 * @create: 2020-05-05 04:20
 **/
public class Main {
    /*public static void main(String[] args) {
        Redis redis = new Redis();
        redis.execute(jedis->{
            System.out.println(jedis.ping());
        });

    }*/

    public static void main(String[] args) {
        Redis redis = new Redis();
/*        redis.execute(new CallWithJedis() {
            @Override
            public void call(Jedis jedis) {
                System.out.println(jedis.ping());
            }
        });*/

    }
}
