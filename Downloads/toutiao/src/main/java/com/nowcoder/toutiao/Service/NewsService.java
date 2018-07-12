package com.nowcoder.toutiao.Service;


import com.nowcoder.toutiao.DAO.CommentDAO;
import com.nowcoder.toutiao.DAO.NewsDAO;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.util.Toutiao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class NewsService {

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    RelationService relationService;

    public String saveImage(MultipartFile file) throws IOException{
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if(dotpos<0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotpos+1).toLowerCase();
        if(! Toutiao.isfileAllowed(fileExt)){
            return null;
        }

        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(), new File(Toutiao.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return Toutiao.Toutiao_Domain + "image?name=" +fileName;
    }

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public List<News> getAllLatestNews(int offset, int limit) {
        return newsDAO.selectByOffset(offset, limit);
    }

    public List<News> getNewsByOfficial(int isOfficial,int offset, int limit) {
        return newsDAO.selectByOfficialAndOffset(isOfficial,offset,limit);
    }

    public List<News> getFollowNews(int userId){
        Set<String> followList = relationService.getfollowList(userId,0,100000);
        List<News> followNewsList = new ArrayList<>();
        for(String followId : followList){
            List<News> newsList = getLatestNews(Integer.parseInt(followId),0,100000);
            for(News news : newsList){
                followNewsList.add(news);
            }
        }
        followNewsList.sort(new Comparator<News>() {
            @Override
            public int compare(News o1, News o2) {
                return (int)(o2.getCreatedDate().getTime() - o1.getCreatedDate().getTime());
            }
        });
        return followNewsList;
    }

    public int updateCommentCount(int id, int count){
        return newsDAO.updateCommentCount(id,count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int id){
        return newsDAO.getById(id);
    }

    public void deleteNews(int newsId){
        newsDAO.deleteNews(newsId,0);
    }

    public void deleteComments(int id){
        commentDAO.deleteComment(id,0);
    }
}
