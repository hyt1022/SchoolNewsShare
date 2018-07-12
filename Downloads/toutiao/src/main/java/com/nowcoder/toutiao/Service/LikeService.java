package com.nowcoder.toutiao.Service;


import com.nowcoder.toutiao.util.JedisAdaptor;
import com.nowcoder.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        if(jedisAdaptor.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }else if(jedisAdaptor.sismember(dislikeKey,String.valueOf(userId))){
            return -1;
        }else
            return 0;
    }

    public long like(int userId,int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdaptor.sadd(likeKey,String.valueOf(userId));
        jedisAdaptor.srem(dislikeKey,String.valueOf(userId));
        return jedisAdaptor.scard(likeKey);
    }

    public long dislike(int userId,int entityType, int entityId){

        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdaptor.sadd(dislikeKey,String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdaptor.srem(likeKey,String.valueOf(userId));
        return jedisAdaptor.scard(likeKey);
    }






}
