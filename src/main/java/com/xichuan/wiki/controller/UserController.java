package com.xichuan.wiki.controller;

import com.alibaba.fastjson.JSON;
import com.xichuan.wiki.req.UserLoginReq;
import com.xichuan.wiki.req.UserQueryReq;
import com.xichuan.wiki.req.UserResetPasswordReq;
import com.xichuan.wiki.req.UserSaveReq;
import com.xichuan.wiki.resp.CommonResp;
import com.xichuan.wiki.resp.UserLoginResp;
import com.xichuan.wiki.resp.UserQueryResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.service.UserService;
import com.xichuan.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param userQueryReq
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq userQueryReq) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userQueryReq);
        resp.setContent(list);
        return resp;
    }

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param userSaveReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq userSaveReq) {
        userSaveReq.setPassword(DigestUtils.md5DigestAsHex(userSaveReq.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(userSaveReq);
        return resp;
    }

    /***
     * 删除接口
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    /***
     * 重置密码
     * @param userResetPasswordReq
     * @return
     */
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq userResetPasswordReq) {
        userResetPasswordReq.setPassword(DigestUtils.md5DigestAsHex(userResetPasswordReq.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(userResetPasswordReq);
        return resp;
    }

    /***
     * 登录
     * @param userLoginReq
     * @return
     */
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq userLoginReq) {
        userLoginReq.setPassword(DigestUtils.md5DigestAsHex(userLoginReq.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(userLoginReq);
        Long token = snowFlake.nextId();
        userLoginResp.setToken(token.toString());
        log.info("生成单点登录token{},并放入redis中", token);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(userLoginResp), 3600*24, TimeUnit.SECONDS);
        resp.setContent(userLoginResp);
        return resp;
    }


    /***
     * 删除接口
     * @param token
     * @return
     */
    @GetMapping("/logout/{token}")
    public CommonResp delete(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        log.info("从redis中删除token：{}", token);
        return resp;
    }
}
