package com.gilxyj.hyperloglog;

import redis.clients.jedis.Jedis;

public interface CallWithJedis {
    void call(Jedis jedis);
}
