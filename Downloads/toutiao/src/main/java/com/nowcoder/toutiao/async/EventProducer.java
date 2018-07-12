package com.nowcoder.toutiao.async;


import com.alibaba.fastjson.JSON;
import com.nowcoder.toutiao.util.JedisAdaptor;
import com.nowcoder.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public boolean fireEvent(EventModel model){
        try {
            String json = JSON.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdaptor.lpush(key, json);
            return true;
        }catch(Exception e){
            return false;
        }

    }
}
