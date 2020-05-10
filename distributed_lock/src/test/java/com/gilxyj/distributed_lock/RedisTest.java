package com.gilxyj.distributed_lock;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.params.SetParams;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

import static com.gilxyj.distributed_lock.ThreadTest.CHECK_VALUE_EQUAL_COMMAND;

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
 * @create: 2020-05-05 20:00
 **/
public class RedisTest {




    /**
     * 代码存在一个小小问题：如果代码业务执行的过程中抛异常或者挂了，这样会导致del 指令没有被调用，这样，k1 无法释放，后面来的请求全部堵塞在这里，锁也永远得不到释放。
     */
    @Test
    public void test01(){
        Redis redis = new Redis();
        redis.execute(jedis->{
            Long setnx = jedis.setnx("k1", "v1");
            if (setnx==1) {
                //没人占位
                jedis.set("name" ,"javaboy");
                String name = jedis.get("name");
                System.out.println(name);
                jedis.del("k1");
            }else{
                //有人占位。。。
            }
        });
    }

    /**
     * 这段代码还有一个问题，就是在获取锁和设置过期时间之间如果如果服务器突然挂掉了，这个时候锁被占用，无法及时得到释放，也会造成死锁，因为获取锁和设置过期时间是两个操作，不具备原子性。
     */
    @Test
    public void test02(){
        Redis redis = new Redis();
        redis.execute(jedis->{
            Long setnx = jedis.setnx("k1", "v1");
            if (setnx==1) {
                //给锁添加一个过期时间，防止发生异常造成死锁
                jedis.expire("k1", 5);
                //没人占位
                jedis.set("name" ,"javaboy");
                String name = jedis.get("name");
                System.out.println(name);
                jedis.del("k1");
            }else{
                //有人占位。。。
            }
        });
    }

    /**
     * 从Redis2.8 开始，setnx 和expire 可以通过一个命令一起来执行了
     * 为了防止业务代码在执行的时候抛出异常，我们给每一个锁添加了一个超时时间，超时之后，锁会被自动释放，但是这也带来了一个新的问题：如果要执行的业务非常耗时，可能会出现紊乱。举个例子：第一个线程首先获取到锁，然后开始执行业务代码，但是业务代码比较耗时，执行了8 秒，这样，会在第一个线程的任务还未执行成功锁就会被释放了，此时第二个线程会获取到锁开始执行，在第二个线程刚执行了3 秒，第一个线程也执行完了，此时第一个线程会释放锁，但是注意，它释放的第二个线程的锁，释放之后，第三个线程进来。
     */
    @Test
    public void test03(){
        Redis redis = new Redis();
        redis.execute(jedis->{
            SetParams setParams = new SetParams().nx().ex(5);
            String set = jedis.set("k1", "v1", setParams);
            if (set!=null&&"OK".equalsIgnoreCase(set)) {
                //给锁添加一个过期时间，防止发生异常造成死锁
                jedis.expire("k1", 5);
                //没人占位
                jedis.set("name" ,"javaboy");
                String name = jedis.get("name");
                System.out.println(name);
                jedis.del("k1");
            }else{
                //有人占位。。。
            }
        });
    }

    /**
     * 解决上述代码的超时问题
     */
    @Test
    public void test04(){
        Redis redis = new Redis();
        for (int i = 0; i < 10; i++) {
            redis.execute(jedis->{
                String value = UUID.randomUUID().toString();
                SetParams setParams = new SetParams().nx().ex(15);
                String set = jedis.set("k1", value, setParams);
                if (set!=null&&"OK".equalsIgnoreCase(set)) {
                    //没人占位
                    jedis.set("name" ,"javaboy");
                    String name = jedis.get("name");
                    System.out.println(name );
                    //释放锁
                    jedis.evalsha(CHECK_VALUE_EQUAL_COMMAND, Arrays.asList("k1"), Arrays.asList(value));
                }else{
                    //有人占位。。。
                    System.out.println("someone use the lock");
                }
            });
        }

    }

    /**
     * 多线程调用
     * @throws InterruptedException
     */
    @Test
    public void test05() throws InterruptedException {
        Redis redis = new Redis();
        ThreadTest test1 = new ThreadTest("test1",redis);
        ThreadTest test2 = new ThreadTest("test2",redis);
        ThreadTest test3 = new ThreadTest("test3",redis);
        ThreadTest test4 = new ThreadTest("test4",redis);
        ThreadTest test5 = new ThreadTest("test5",redis);
        test1.start();
        test2.start();
        test3.start();
        test4.start();
        test5.start();
        //等待所有线程结束 test1.join();
        Thread.sleep(10000);

    }

    /**
     * 直接在Java 端去写Lua 脚本，写好之后，需要执行时，每次将脚本发送到Redis 上去执行
     */
    @Test
    public void test06(){
        Redis redis = new Redis();
        for (int i = 0; i < 10; i++) {
            redis.execute(jedis->{
                String value = UUID.randomUUID().toString();
                SetParams setParams = new SetParams().nx().ex(5);
                String set = jedis.set("k1", value, setParams);
                StringBuffer sb= null;
                if (set!=null&&"OK".equalsIgnoreCase(set)) {
                    //没人占位
                    //获取文件
                    try(InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("releasewherevalueequal.lua");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))){
                        sb=new StringBuffer();
                        String s="";
                        while ((s=bufferedReader.readLine())!=null){
                            sb.append(s);
                            sb.append("\n");
                        }
                        //System.out.println(sb.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    jedis.set("name" ,"javaboy");
                    String name = jedis.get("name");
                    System.out.println(name );
                    //释放锁
                    jedis.eval(sb.toString(), Arrays.asList("k1"), Arrays.asList(value));
                }else{
                    //有人占位。。。
                    System.out.println("someone use the lock");
                }
            });
        }

    }

    public void test07(){

    }

}
