package com.mmall.controller;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private IUserService iUserService;
    /*
     *用戶登錄
     * @param username
     * @param password
     * @param session
     * @return
     * */
@RequestMapping(value ="login.do",method = RequestMethod.POST)
@ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
    ServerResponse<User > response=iUserService.login(username,password);
    if(response.isSuccess()){
        session.setAttribute(Const.CURRENT_USER,response.getData());
    }
        return response;
    }

    @RequestMapping(value="logout.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess("退出成功");
    }
    //注册用户
    @RequestMapping(value="register.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    //检验
    @RequestMapping(value ="check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    @RequestMapping(value ="get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
    User user=(User)session.getAttribute(Const.CURRENT_USER);
    if(user!=null){
        return ServerResponse.createBySuccess(user);
    }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    //忘记密码
    @RequestMapping(value ="forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    //问题确认
    @RequestMapping(value ="forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }

    //忘记密码，密码重置
    @RequestMapping(value ="forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }

    //密码重置
    @RequestMapping(value ="reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> RestPassword(HttpSession session,String passwordOld,String passwordNew){
       User user=(User)session.getAttribute(Const.CURRENT_USER);
       if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录");
       }
       return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    //更新用户信息
    @RequestMapping(value ="update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response=iUserService.updateInfomation(user);

        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value ="get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
