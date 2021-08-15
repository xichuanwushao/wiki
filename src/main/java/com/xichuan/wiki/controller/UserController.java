package com.xichuan.wiki.controller;

import com.xichuan.wiki.req.UserQueryReq;
import com.xichuan.wiki.req.UserResetPasswordReq;
import com.xichuan.wiki.req.UserSaveReq;
import com.xichuan.wiki.resp.CommonResp;
import com.xichuan.wiki.resp.UserQueryResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

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
     * @param userSaveReq
     * @return
     */
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq userResetPasswordReq) {
        userResetPasswordReq.setPassword(DigestUtils.md5DigestAsHex(userResetPasswordReq.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(userResetPasswordReq);
        return resp;
    }


}
