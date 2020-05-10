package com.gilxyj.redis_delay_queue;

import java.util.Queue;

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
 * @create: 2020-05-07 00:57
 **/
public class ProducerThread implements Runnable{

    DelayMsgQueue delayMsgQueue;

    String name;


    public ProducerThread(String name,DelayMsgQueue delayMsgQueue) {
        this.name = name;
        this.delayMsgQueue = delayMsgQueue;
    }

    @Override
    public void run() {
            for (int i = 0; i < 100; i++) {
                delayMsgQueue.queue("good boy:"+name+i);
            }

    }
}
