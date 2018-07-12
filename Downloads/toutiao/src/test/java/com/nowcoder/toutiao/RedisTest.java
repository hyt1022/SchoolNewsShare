package com.nowcoder.toutiao;

import redis.clients.jedis.Jedis;

public class RedisTest {

    public static void print(Object obj){
        System.out.println(obj.toString());
    }

    public static void main(String args[]){

        Jedis jedis = new Jedis();
        jedis.set("a","b");
        print(jedis.get("a"));

        jedis.set("pv","100");
        jedis.incr("pv");
        print(jedis.get("pv"));
        jedis.incrBy("pv",5);


        String list = "List";
        for(int i = 0; i<10;i++){
            jedis.lpush(list,"a"+String.valueOf(i));
        }
        print(jedis.lrange(list,0,10));

        String userKey = "userxx";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","20");

        print(jedis.hgetAll(userKey));

        print(jedis.hlen(userKey));




    }
}
