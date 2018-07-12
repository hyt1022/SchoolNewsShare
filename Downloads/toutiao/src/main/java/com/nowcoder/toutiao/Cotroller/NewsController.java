package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.DAO.CommentDAO;
import com.nowcoder.toutiao.DAO.NewsDAO;
import com.nowcoder.toutiao.Service.*;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventProducer;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.*;
import com.nowcoder.toutiao.util.Toutiao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    CollectionService collectionService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId,
                             @RequestParam(value = "page" ,defaultValue = "1") int page, Model model) {
        News news = newsService.getById(newsId);
        if (news != null) {
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, Entitytype.Entity_NEWS, news.getId()));
                model.addAttribute("isCollected",collectionService.getCollectStatus(localUserId,news.getId()));
            } else {
                model.addAttribute("like", 0);
            }
            // 评论
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), Entitytype.Entity_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                //vo.set("comment", comment);
                //vo.set("user", userService.getUser(comment.getUserId()));
                vo.setComment(comment);
                vo.setUser(userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments", commentVOs);
            model.addAttribute("size",(comments.size()/10)+1);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        model.addAttribute("page",page);
        return "detail-test";
    }


    @RequestMapping(value = "/image",method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(Toutiao.IMAGE_DIR+imageName)),
                    response.getOutputStream());
        }catch(Exception e){
            logger.error("加载图片失败" + e.getMessage());
        }
    }

    @RequestMapping(value = "/uploadImage",method = {RequestMethod.POST})
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl = newsService.saveImage(file);
            if(fileUrl == null){
                return Toutiao.getJSONString(1,"上传图片失败");
            }
            return Toutiao.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return Toutiao.getJSONString(1,"上传失败");
        }
    }

    @RequestMapping(value= "/user/addNews" , method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link,
                          @RequestParam("content") String content){
        try{
            News news = new News();
            news.setCreatedDate(new Date());
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setContent(content);
            news.setStatus(1);
            if(hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                news.setUserId(30);
            }
            newsService.addNews(news);
            return Toutiao.getJSONString(0);
        } catch(Exception e){
            logger.error("添加咨询失败"+e.getMessage());
            return Toutiao.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(value="/addComment", method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(Entitytype.Entity_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(1);
            News news = newsService.getById(newsId);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(newsId,Entitytype.Entity_NEWS);
            newsService.updateCommentCount(newsId,count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                    .setEntityType(Entitytype.Entity_NEWS).setEntityOwnerId(news.getUserId()));
        }catch(Exception e){
            logger.error("添加评论失败"+e.getMessage());
        }

        return "redirect:/news/"+ String.valueOf(newsId);

    }

    @RequestMapping(value="/deleteNews")
    @ResponseBody
    public String deleteNews(@RequestParam("newsId") int newsId){
        try{
            newsService.deleteNews(newsId);
            return Toutiao.getJSONString(0,"删除成功");
        }catch (Exception e){
            logger.error("删除失败"+e.getMessage());
            return Toutiao.getJSONString(1,"删除失败");
        }
    }

    @RequestMapping(value="/deleteComments", method = {RequestMethod.POST})
    @ResponseBody
    public String deleteComments(@RequestParam("commentId") int commentId){
        try{
            newsService.deleteComments(commentId);
            return Toutiao.getJSONString(0,"删除成功");
        }catch (Exception e){
            logger.error("删除失败"+e.getMessage());
            return Toutiao.getJSONString(1,"删除失败");
        }
    }





}
