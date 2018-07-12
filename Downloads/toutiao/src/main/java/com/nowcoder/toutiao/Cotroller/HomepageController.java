package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.Service.NewsService;
import com.nowcoder.toutiao.Service.RelationService;
import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.model.*;
import com.nowcoder.toutiao.util.RedisKeyUtil;
import com.nowcoder.toutiao.util.Toutiao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class HomepageController {
    private static final Logger logger = LoggerFactory.getLogger(HomepageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    RelationService relationService;

    private void setBasicElement(int userId,Model model){
        //ViewObject voUser = new ViewObject();
        if(hostHolder.getUser() != null){
            if(hostHolder.getUser().getId() == userId){
                //System.out.println(String.format("%d_%d",hostHolder.getUser().getId(),userId));
                model.addAttribute("isUser",true);
            }else {
                //System.out.println(userId);
                model.addAttribute("isUser",false);
               model.addAttribute("isFollowed",relationService.isFollowed(hostHolder.getUser().getId(),userId));
            }
        }else{
            model.addAttribute("isUser",false);
        }
        User user = userService.getUser(userId);
        user.setFansCount((int)relationService.fansCount(userId));
        user.setFollowCount((int)relationService.followCount(userId));
        model.addAttribute("userInfo",user);
    }

    @RequestMapping(value = "/uploadHead",method = {RequestMethod.GET,RequestMethod.POST})
    public String upload(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl = newsService.saveImage(file);
            if(fileUrl == null){
                return "redirect:/user/edit";
            }
            hostHolder.getUser().setHeadUrl(fileUrl);
            userService.updateHeadUrl(hostHolder.getUser().getId(),fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
        }
        return "redirect:/user/edit";
    }

    @RequestMapping(path = {"/user/{userId}"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String personalPage(@PathVariable("userId") int userId, @RequestParam(value="page",defaultValue = "1") int page,
                        Model model){
        setBasicElement(userId,model);
        return "personInfo";
    }

    @RequestMapping(path = {"/user/{userId}/collection"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String personalCollection(@PathVariable("userId") int userId,@RequestParam(value="page",defaultValue = "1") int page,
                                     Model model){
        try{
            setBasicElement(userId,model);
            Jedis jedis = new Jedis();
            Set<String> cnewsList = jedis.smembers(RedisKeyUtil.getBizCollection(userId));

            List<ViewObject> vos = new ArrayList<>();
            for(String newsId : cnewsList){
                ViewObject vo = new ViewObject();
                News news = newsService.getById(Integer.parseInt(newsId));
                vo.setNews(news);
                vos.add(vo);
            }
            model.addAttribute("page",page);
            model.addAttribute("size",(cnewsList.size()/10)+1);
            model.addAttribute("collectionNews",vos);
            //return Toutiao.getJSONString(cnewsList.size());
        }catch(Exception e){
            logger.error("收藏异常" + e.getMessage());
            //return Toutiao.getJSONString(1,"收藏异常");
        }
        return "personInfo-collection";
    }

    @RequestMapping(path = {"/user/{userId}/latest"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String personallatest(@PathVariable("userId") int userId,@RequestParam(value="page",defaultValue = "1") int page,
                                     Model model){
        try{
            setBasicElement(userId,model);
            List<News> lnewsList = newsService.getLatestNews(userId,(page-1)*10,page*10);
            List<ViewObject> vos = new ArrayList<>();
            for(News news : lnewsList){
                ViewObject vo = new ViewObject();
                vo.setNews(news);
                vos.add(vo);
            }
            model.addAttribute("page",page);
            model.addAttribute("size",(lnewsList.size()/10)+1);
            model.addAttribute("latestNews",vos);
            //return Toutiao.getJSONString(lnewsList.size());
        }catch(Exception e){
            logger.error("收藏异常" + e.getMessage());
            //return Toutiao.getJSONString(1,"收藏异常");
        }
        return "personInfo-latest";
    }

    @RequestMapping(path = {"/user/{userId}/follow"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String personalFollow(@PathVariable("userId") int userId, @RequestParam(value="page",defaultValue = "1") int page,
                                 Model model){
        try{
            setBasicElement(userId,model);
            //Jedis jedis = new Jedis();
            Set<String> followList = relationService.getfollowList(userId,(page-1)*10,page*10);
            List<ViewObject> vos = new ArrayList<>();
            for(String followId : followList){
                ViewObject vo = new ViewObject();
                User user = userService.getUser(Integer.parseInt(followId));
                vo.setUser(user);
                vo.setFollowed(relationService.isFollowed(hostHolder.getUser().getId(),Integer.parseInt(followId)));
                vos.add(vo);
            }
            System.out.println("数量为："+relationService.followCount(userId));
            model.addAttribute("page",page);
            model.addAttribute("size",(relationService.followCount(userId)/10 + 1));
            model.addAttribute("followUsers",vos);
        }catch (Exception e){
            logger.error("显示异常"+e.getMessage());
        }
        return "personInfo-follow";
    }

    @RequestMapping(path = {"/user/{userId}/fans"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String personalFans(@PathVariable("userId") int userId, @RequestParam(value="page",defaultValue = "1") int page,
                                 Model model){
        try{
            setBasicElement(userId,model);
            Set<String> fansList = relationService.getfansList(userId,(page-1)*10,page*10);
            List<ViewObject> vos = new ArrayList<>();
            for(String fansId : fansList){
                ViewObject vo = new ViewObject();
                User user = userService.getUser(Integer.parseInt(fansId));
                vo.setUser(user);
                vo.setFollowed(relationService.isFollowed(hostHolder.getUser().getId(),Integer.parseInt(fansId)));
                vos.add(vo);
            }
            model.addAttribute("page",page);
            model.addAttribute("size",(relationService.getUserFansCount(userId)/10)+1);
            model.addAttribute("fansUsers",vos);
        }catch (Exception e){
            logger.error("显示异常"+e.getMessage());
        }
        return "personInfo-fans";
    }

    @RequestMapping(path={"user/edit"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String personEdit(){
        return "personInfo-edit";
    }


    @RequestMapping(path={"user/saveEdit"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String personSaveEdit(@RequestParam("username") String username,
                                 @RequestParam("school") String school,
                                 @RequestParam("age") int age,
                                 @RequestParam("intro") String intro,
                                 @RequestParam("sex") int sex){
        int localUserId = hostHolder.getUser().getId();
        User user = userService.getUser(localUserId);
        user.setUsername(username);
        user.setSex(sex);
        user.setAge(age);
        user.setSchool(school);
        user.setIntro(intro);
        userService.updateUserInfo(localUserId,username,sex,age,intro,school);
        return "redirect:/user/" + String.valueOf(localUserId);
    }
}
