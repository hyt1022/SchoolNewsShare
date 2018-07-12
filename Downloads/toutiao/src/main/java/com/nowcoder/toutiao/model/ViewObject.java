package com.nowcoder.toutiao.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {

    private Map<String, Object> objs = new HashMap<String, Object>();
    private User user;
    private Comment comment;
    private News news;
    private Message message;
    private int unreadCount;
    private int likeStatus;
    private boolean isUserHimself;
    private boolean isFollowed;

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public boolean isUserHimself() {
        return isUserHimself;
    }

    public void setUserHimself(boolean userHimself) {
        isUserHimself = userHimself;
    }



    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }



    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }

}
