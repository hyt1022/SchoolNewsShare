package com.nowcoder.toutiao.Service;

import com.nowcoder.toutiao.util.JedisAdaptor;
import com.nowcoder.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class CollectionService {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public long collect(int userId,int newsId){
        String userCollectKey = RedisKeyUtil.getBizCollection(userId);
        jedisAdaptor.sadd(userCollectKey,String.valueOf(newsId));
        return jedisAdaptor.scard(userCollectKey);
    }

    public long disCollect(int userId,int newsId){
        String userCollectKey = RedisKeyUtil.getBizCollection(userId);
        jedisAdaptor.srem(userCollectKey,String.valueOf(newsId));
        return jedisAdaptor.scard(userCollectKey);
    }

    public boolean getCollectStatus(int userId,int newsId){
        String userCollectKey = RedisKeyUtil.getBizCollection(userId);
        return jedisAdaptor.sismember(userCollectKey,String.valueOf(newsId));
    }

}
