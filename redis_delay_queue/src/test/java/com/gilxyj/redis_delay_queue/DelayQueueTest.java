package com.gilxyj.redis_delay_queue;

import org.junit.Test;

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
 * @create: 2020-05-07 01:17
 **/
public class DelayQueueTest {


    @Test
    public void test01() throws InterruptedException {
        Redis redis = new Redis();
        DelayMsgQueue queueDemo = new DelayMsgQueue(redis, "queueDemo");
        ProducerThread product1 = new ProducerThread("product1",queueDemo);
        //ProducerThread product2 = new ProducerThread("product2", queueDemo);
        ConsumerThread consumer1 = new ConsumerThread("consumer1", queueDemo);
        ConsumerThread consumer2 = new ConsumerThread("consumer2", queueDemo);
        Thread productThread1 = new Thread(product1, "product1");
        //Thread productThread2 = new Thread(product2, "product2");
        Thread consumerThread1 = new Thread(consumer1, "consumer1");
        Thread consumerThread2 = new Thread(consumer2, "consumer2");

        productThread1.start();
        //productThread2.start();
        consumerThread1.start();
        consumerThread2.start();
        Thread.sleep(100000);
        consumerThread1.interrupt();
        consumerThread2.interrupt();
        System.out.println(Thread.currentThread().getName()+"end");
    }
    @Test
    public void test02(){
        int reverse = reverse(Integer.MIN_VALUE);
        System.out.println(reverse);
    }

    public int reverse(int x) {
        long abs = Math.abs((long) x);
        String s = String.valueOf(abs);
        StringBuffer sb = new StringBuffer(s);
        StringBuffer reverse = sb.reverse();
        if ((x>0&&Long.valueOf(reverse.toString())>Integer.MAX_VALUE)||(x<0&&Long.valueOf(reverse.toString())>Math.abs((long) Integer.MIN_VALUE))) {
            return 0;
        }
        return x >= 0 ? Integer.valueOf(reverse.toString()):Integer.valueOf(reverse.toString())*-1;
    }
}
