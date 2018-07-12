package com.nowcoder.toutiao.util;



public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "like";
    private static String BIZ_DISLIKE = "dislike";
    private static String BIZ_EVENT = "event";
    private static String BIZ_COLLECTION = "collect";
    private static String BIZ_FOLLOW = "follow";
    private static String BIZ_FANS = "fans";

    public static String getLikeKey(int entity_type,int entity_id){
        return BIZ_LIKE + SPLIT +String.valueOf(entity_type)+ SPLIT +String.valueOf(entity_id);
    }

    public static String getDislikeKey(int entity_type,int entity_id){
        return BIZ_DISLIKE + SPLIT +String.valueOf(entity_type)+ SPLIT +String.valueOf(entity_id);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    public static String getBizCollection(int userid){
        return BIZ_COLLECTION + SPLIT + String.valueOf(userid);
    }

    public static String getFansKey(int userid){
        return String.valueOf(userid) + SPLIT +BIZ_FANS;
    }

    public static String getFollowKey(int userid){
        return String.valueOf(userid) + SPLIT +BIZ_FOLLOW;
    }

}
