package com.nowcoder.toutiao.util;



import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;


@Service
public class JedisAdaptor implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdaptor.class);
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }

    public Jedis getJedis(){
        return pool.getResource();
    }


    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }


    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public long zadd(String key,long score,String member){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zadd(key,score,member);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long zrem(String key,String member){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrem(key,member);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long zcard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zcard(key);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public Set<String> zrevrange(String key, int start, int stop){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrevrange(key,start,stop);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public boolean zismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            Set<String> set = jedis.zrange(key,0,-1);
            return set.contains(value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }



    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object obj){
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key,Class<T> clazz){
        String value = get(key);
        if(value != null){
            return JSON.parseObject(value,clazz);
        }
        return null;
    }



}
