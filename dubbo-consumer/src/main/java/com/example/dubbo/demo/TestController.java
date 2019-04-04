package com.example.dubbo.demo;

import com.examle.core.common.Constants;
import com.examle.core.rpc.RpcContext;
import com.example.api.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/demo")
    public ModelAndView testDemo() {
        ModelAndView modelAndView = new ModelAndView("index");
        RpcContext.getContext().setAttachment(Constants.TAG_KEY, "gray");
        modelAndView.addObject("result", demoService.sayHello("Dubbo Meetup"));
        return modelAndView;
    }
}
