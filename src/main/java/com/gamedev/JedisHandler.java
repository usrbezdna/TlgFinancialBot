package com.gamedev;
import redis.clients.jedis.Jedis;

public class JedisHandler {
    static Jedis db = new Jedis("185.239.208.162", 6379);
    public static void pingBase(){
        System.out.println(db.ping());
    }
}
