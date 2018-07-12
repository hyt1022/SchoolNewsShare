package com.nowcoder.toutiao.Cotroller;

import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.util.Toutiao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;



    @RequestMapping("/regist")
    public String regist(){
        return "regist";
    }

    @RequestMapping("/reg")
    public String reg(Model model,
                    @RequestParam("username") String username,
                    @RequestParam("password") String password,
                    @RequestParam("makesurepwd") String makesurepwd,
                      @RequestParam("nickname") String nickname,
                      @RequestParam("school") String school,
                      @RequestParam("age") int age,
                      @RequestParam("sex") int sex,
                    @RequestParam(value="rember", defaultValue="0") int remember,
                    HttpServletResponse response){
        try{
            if(!StringUtils.equals(makesurepwd,password)){
                return "regist";
            }else{
                Map<String,Object> map = userService.register(username,password,nickname,school,age,sex);
                if(map.containsKey("ticket")){
                    Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                    cookie.setPath("/");
                    if(remember>0){
                        cookie.setMaxAge(3600*24*5);
                    }
                    response.addCookie(cookie);
                }else{
                    //return Toutiao.getJSONString(0,map);
                }
            }
        }
        catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            //return Toutiao.getJSONString(1, "注册异常");
        }
        return "redirect:/";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                return Toutiao.getJSONString(0,"登陆成功");
            }else{
                return Toutiao.getJSONString(0,map);
            }
        }
        catch (Exception e){
            logger.error("登陆异常" + e.getMessage());
            return Toutiao.getJSONString(1, "登陆异常");
        }
    }


    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }



}
