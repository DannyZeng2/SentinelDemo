package com.oocl.sentinel.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FlowControlController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     */
    @GetMapping("/getUser")
    @ResponseBody
    public UserService.User getUser(@RequestParam("uid") Long uid) {
        return userService.getUser(uid);
    }

}