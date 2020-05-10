package com.gilxyj.redis_delay_queue;

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
 * @create: 2020-05-07 01:11
 **/
public class ConsumerThread implements Runnable{

    private String name;
    private DelayMsgQueue delayMsgQueue;

    public ConsumerThread(String name, DelayMsgQueue delayMsgQueue) {
        this.name = name;
        this.delayMsgQueue = delayMsgQueue;
    }

    @Override
    public void run() {
         delayMsgQueue.loop();
    }
}
