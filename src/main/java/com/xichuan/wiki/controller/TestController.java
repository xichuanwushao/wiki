package com.xichuan.wiki.controller;

import com.xichuan.wiki.domain.Test;
import com.xichuan.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Resource
    private TestService testService;

    @Value("${test.hello:TEST}")//
    private String testHello;
    /**
     * GET, POST, PUT, DELETE
     *
     * /user?id=1
     * /user/1
     * @return
     */
    //@RequestMapping("/hello")
    // @GetMapping("/hello")
    // @PostMapping("/hello")
    // @PutMapping("/hello")
    // @DeleteMapping("/hello")
    // @RequestMapping(value = "/user/1", method = RequestMethod.GET)
    // @RequestMapping(value = "/user/1", method = RequestMethod.POST)
    // @RequestMapping(value = "/user/1", method = RequestMethod.PUT)
    // @RequestMapping(value = "/user/1", method = RequestMethod.DELETE)
    // @RequestMapping包含了@GetMapping@PostMapping@PutMapping@DeleteMapping的四个功能
    // 写法一 @GetMapping("/hello")
    // 写法二 @RequestMapping(value = "/user/1", method = RequestMethod.GET)
    // http://127.0.0.1:8080/hello
    //@PostMapping("/hello") 接口只支持POST请求 而浏览器属于GET请求 所以不支持 报错405
    //2021-07-25 12:03:04.028 WARN  o.s.w.s.m.support.DefaultHandlerExceptionResolver :207  [32m                  [0;39m Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not supported]
    @GetMapping("/hello")
    public String hello() {
        return "hello wikis !" + testHello;
    };

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello wikis ! Post "+name;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}
