package com.nowcoder.toutiao;


import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.model.customer;
import com.nowcoder.toutiao.util.JedisAdaptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTests {

    @Autowired
    JedisAdaptor jedisAdaptor;

    @Test
    public void testObject(){
        customer c = new customer();
        c.setFirstname("huang");
        c.setLastname("yitong");

        jedisAdaptor.setObject("user1", c);

        customer c1 = jedisAdaptor.getObject("user1", customer.class);

    }

    @Test
    public void testzismember(){
        String key = "12:follow";
        System.out.println(jedisAdaptor.zismember(key,String.valueOf(1)));
        System.out.println(jedisAdaptor.zismember(key,String.valueOf(2)));
        System.out.println(jedisAdaptor.zismember(key,String.valueOf(3)));
        System.out.println(jedisAdaptor.zismember(key,String.valueOf(6)));
        System.out.println(jedisAdaptor.zismember(key,String.valueOf(7)));
    }
}
