package com.nowcoder.toutiao.Service;


import com.nowcoder.toutiao.DAO.RelationDAO;
import com.nowcoder.toutiao.model.Relation;
import com.nowcoder.toutiao.util.JedisAdaptor;
import com.nowcoder.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RelationService {

    @Autowired
    RelationDAO relationDAO;

    @Autowired
    JedisAdaptor jedisAdaptor;


    public int addRelation(Relation relation){
        return relationDAO.addRelation(relation);
    }

    public void deleteByFromAndToId(int fromId,int toId){
        relationDAO.deleteByFromAndToID(fromId,toId);
    }

    public int getUserFansCount(int toId){
        return relationDAO.getUserFansCount(toId);
    }

    public int getUserFollowCount(int toId){
        return relationDAO.getUserFollowCount(toId);
    }


    public long follow(int userId, long time, int followId){
        String followKey = RedisKeyUtil.getFollowKey(userId);
        String fansKey = RedisKeyUtil.getFansKey(followId);
        jedisAdaptor.zadd(followKey,time,String.valueOf(followId));
        jedisAdaptor.zadd(fansKey,time,String.valueOf(userId));

        return jedisAdaptor.zcard(followKey);
    }

    public long disfollow(int userId, int followId){
        String followKey = RedisKeyUtil.getFollowKey(userId);
        String fansKey = RedisKeyUtil.getFansKey(followId);
        jedisAdaptor.zrem(followKey,String.valueOf(followId));
        jedisAdaptor.zrem(fansKey,String.valueOf(userId));

        return jedisAdaptor.zcard(followKey);
    }

    public long fansCount(int userId){
        String fansKey = RedisKeyUtil.getFansKey(userId);
        return jedisAdaptor.zcard(fansKey);
    }

    public long followCount(int userId){
        String followKey = RedisKeyUtil.getFollowKey(userId);
        return jedisAdaptor.zcard(followKey);
    }

    public Set<String> getfollowList(int userId, int limit, int offset){
        String followKey = RedisKeyUtil.getFollowKey(userId);
        return jedisAdaptor.zrevrange(followKey,limit,offset);
    }

    public Set<String> getfansList(int userId,int limit,int offset){
        String fansKey = RedisKeyUtil.getFansKey(userId);
        return jedisAdaptor.zrevrange(fansKey,limit,offset);
    }

    public boolean isFollowed(int userId,int toId){
        String followKey = RedisKeyUtil.getFollowKey(userId);
        return jedisAdaptor.zismember(followKey,String.valueOf(toId));
    }






}
