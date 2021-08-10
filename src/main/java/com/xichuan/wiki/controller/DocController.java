package com.xichuan.wiki.controller;

import com.xichuan.wiki.req.DocQueryReq;
import com.xichuan.wiki.req.DocSaveReq;
import com.xichuan.wiki.resp.DocQueryResp;
import com.xichuan.wiki.resp.CommonResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param docQueryReq
     * @return
     */
    @GetMapping("/all")
    public CommonResp all(@Valid DocQueryReq docQueryReq) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(docQueryReq);
        resp.setContent(list);
        return resp;
    }
    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param docQueryReq
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq docQueryReq) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docQueryReq);
        resp.setContent(list);
        return resp;
    }

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param docSaveReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq docSaveReq) {
        CommonResp resp = new CommonResp<>();
        docService.save(docSaveReq);
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
        docService.delete(id);
        return resp;
    }
}