package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.Service.CollectionService;
import com.nowcoder.toutiao.Service.NewsService;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Entitytype;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.util.Toutiao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CollectionController {
    private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);

    @Autowired
    CollectionService collectionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @RequestMapping("/collection")
    public String collectNews(@RequestParam("newsId") int newsId){
        try {
            int userId = hostHolder.getUser().getId();
            long collectCount = collectionService.collect(userId,newsId);
            //return Toutiao.getJSONString(0, String.valueOf(collectCount));
        } catch (Exception e) {
            logger.error("收藏异常" + e.getMessage());
            //return Toutiao.getJSONString(1, "收藏异常");
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    @RequestMapping("/disCollection")
    public String discollectNews(@RequestParam("newsId") int newsId){
        try {
            int userId = hostHolder.getUser().getId();
            long collectCount = collectionService.disCollect(userId,newsId);
            //return Toutiao.getJSONString(0, String.valueOf(collectCount));
        } catch (Exception e) {
            logger.error("取消收藏异常" + e.getMessage());
            //return Toutiao.getJSONString(1, "取消收藏异常");
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }
}
