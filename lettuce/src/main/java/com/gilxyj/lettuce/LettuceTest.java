package com.gilxyj.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
 * @create: 2020-05-05 05:53
 **/
public class LettuceTest {
    public static void main(String[] args) {
        //@的十六进制的ASCII为40，%加 @的16进制ASCII码代替 @
        RedisClient redisClient = RedisClient.create("redis://p%40ssw0rd@192.168.133.128");
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> sync = connect.sync();
        sync.set("name", "gilxyj");
        String name = sync.get("name");
        System.out.println(name);

    }
}
