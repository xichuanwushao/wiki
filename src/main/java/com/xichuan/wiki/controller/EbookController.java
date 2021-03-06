package com.xichuan.wiki.controller;

import com.xichuan.wiki.req.EbookQueryReq;
import com.xichuan.wiki.req.EbookSaveReq;
import com.xichuan.wiki.resp.CommonResp;
import com.xichuan.wiki.resp.EbookQueryResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param ebookQueryReq
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq ebookQueryReq) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookQueryReq);
        resp.setContent(list);
        return resp;
    }

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param ebookSaveReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq ebookSaveReq) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(ebookSaveReq);
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
        ebookService.delete(id);
        return resp;
    }
}
