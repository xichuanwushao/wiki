package com.xichuan.wiki.controller;

import com.xichuan.wiki.domain.Test;
import com.xichuan.wiki.service.TestService;
import com.xichuan.wiki.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Resource
    private TestService testService;

    @Value("${test.hello:TEST}")//
    private String testHello;

    @Resource
    private RedisTemplate redisTemplate;
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

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        log.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        log.info("key: {}, value: {}", key, object);
        return object;
    }
}
