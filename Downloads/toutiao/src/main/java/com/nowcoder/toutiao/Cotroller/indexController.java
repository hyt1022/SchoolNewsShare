package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.Service.LikeService;
import com.nowcoder.toutiao.Service.NewsService;
import com.nowcoder.toutiao.Service.RelationService;
import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class indexController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    RelationService relationService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getNews(int offset, int limit){
        List<News> lnewsList=newsService.getAllLatestNews(offset,limit);
        List<ViewObject> vos = new ArrayList<>();
        for (News news:lnewsList){
            ViewObject vo = new ViewObject();
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                //model.addAttribute("like", likeService.getLikeStatus(localUserId, Entitytype.Entity_NEWS, news.getId()));
                vo.setLikeStatus(likeService.getLikeStatus(localUserId, Entitytype.Entity_NEWS, news.getId()));
            } else {
                //model.addAttribute("like", 0);
                vo.setLikeStatus(0);
            }
            vo.setNews(news);
            vo.setUser(userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    private List<ViewObject> getOfficalNews(int isOfficial,int offset, int limit){
        List<News> onewsList=newsService.getNewsByOfficial(isOfficial,offset,limit);
        List<ViewObject> vos = new ArrayList<>();
        for (News news:onewsList){
            ViewObject vo = new ViewObject();
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                vo.setLikeStatus(likeService.getLikeStatus(localUserId, Entitytype.Entity_NEWS, news.getId()));
            } else {
                vo.setLikeStatus(0);
            }
            vo.setNews(news);
            vo.setUser(userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

     private List<ViewObject> getFollowNews(int userId,int offset,int limit){
         List<News> allfollowNews = newsService.getFollowNews(userId);
         int size = allfollowNews.size();
         List<News> followNews = new ArrayList<>();
         if((offset+limit) > size){
             followNews = allfollowNews.subList(offset,size - offset);
         }else{
             followNews = allfollowNews.subList(offset,limit);
         }
         List<ViewObject> vos = new ArrayList<>();
         for(News news : followNews){
             ViewObject vo = new ViewObject();
             int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
             if (localUserId != 0) {
                 vo.setLikeStatus(likeService.getLikeStatus(localUserId, Entitytype.Entity_NEWS, news.getId()));
             } else {
                 vo.setLikeStatus(0);
             }
             vo.setNews(news);
             vo.setUser(userService.getUser(news.getUserId()));
             vos.add(vo);
         }
         return vos;
     }






    @RequestMapping(path = {"/","/index","/index/latestNews"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop,
                        @RequestParam(value = "page" ,defaultValue = "1") int page){
        List<News> newsList = newsService.getAllLatestNews(0,10000);
        model.addAttribute("vos", getNews((page-1)*10, page*10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        model.addAttribute("page",page);
        model.addAttribute("type",1);
        model.addAttribute("size",(newsList.size()/10)+1);

        return "home";
    }

    @RequestMapping(path = {"/index/officialNews"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String indexOfficial(Model model,
                                @RequestParam(value = "pop", defaultValue = "0") int pop,
                                @RequestParam(value = "page" ,defaultValue = "1") int page){
        List<News> onewsList = newsService.getNewsByOfficial(1,0,10000);
        model.addAttribute("vos", getOfficalNews(1,(page-1)*10, page*10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        model.addAttribute("page",page);
        model.addAttribute("type",2);
        model.addAttribute("size",(onewsList.size()/10)+1);
        return "home";
    }

    @RequestMapping(path = {"/index/nonOfficialNews"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String indexNonOfficial(Model model,
                                   @RequestParam(value = "pop", defaultValue = "0") int pop,
                                   @RequestParam(value = "page" ,defaultValue = "1") int page ){
        List<News> newsList = newsService.getNewsByOfficial(0,0,10000);
        model.addAttribute("vos", getOfficalNews(0,(page-1)*10, page*10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        model.addAttribute("page",page);
        model.addAttribute("type",3);
        model.addAttribute("size",(newsList.size()/10)+1);
        return "home";
    }

    @RequestMapping(path= {"/index/followedNews"} , method = {RequestMethod.GET,RequestMethod.POST})
    public String indexFollow(Model model,
                              @RequestParam(value = "pop", defaultValue = "0") int pop,
                              @RequestParam(value = "page" ,defaultValue = "1") int page ){
        int localUserId = hostHolder.getUser().getId();
        List<News> newsList = newsService.getFollowNews(localUserId);
        model.addAttribute("vos",getFollowNews(localUserId,(page-1)*10, page*10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        model.addAttribute("page",page);
        model.addAttribute("type",4);
        model.addAttribute("size",(newsList.size()/10)+1);
        return "home";
    }


    @RequestMapping("/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "coder") String key){
        return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);
    }


    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }



    @RequestMapping(value = "/customer")
    public String saveCustomer(ModelMap model){
        customer c1 = new customer();
        model.addAttribute("customer",c1);
        return "saveCustomer";
    }

    @RequestMapping(value = "/getKey")
    public String getkey(ModelMap model){
        customer c1 = new customer();
        c1.setId("U123");
        model.addAttribute("customer",c1);
        return "getkey";
    }

    @RequestMapping(value = "/postName")
    public String postName(@RequestParam("firstname") String firstname,
                           @RequestParam("lastname") String lastname,
                           Model model){
        customer c1 = new customer();
        c1.setFirstname(firstname);
        c1.setLastname(lastname);
        model.addAttribute("customer",c1);
        return "postName";
    }
}



