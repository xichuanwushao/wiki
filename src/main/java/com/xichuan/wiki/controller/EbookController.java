package com.xichuan.wiki.controller;

import com.xichuan.wiki.domain.Ebook;
import com.xichuan.wiki.req.EbookReq;
import com.xichuan.wiki.resp.CommonResp;
import com.xichuan.wiki.resp.EbookResp;
import com.xichuan.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list() {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> list = ebookService.list();
        resp.setContent(list);
        return resp;
    }

    /***
     * 模糊查询
     * @param name
     * @return
     */
    @GetMapping("/listByName")
    public CommonResp list(String name) {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> list = ebookService.list( name);
        resp.setContent(list);
        return resp;
    }

    /***
     * 参数使用实体进行模糊查询
     * @param ebookReq
     * @return
     */
    @GetMapping("/listByEntity")
    public CommonResp list(EbookReq ebookReq) {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> list = ebookService.list( ebookReq);
        resp.setContent(list);
        return resp;
    }

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param ebookReq
     * @return
     */
    @GetMapping("/listResp")
    public CommonResp listResp(EbookReq ebookReq) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.listResp( ebookReq);
        resp.setContent(list);
        return resp;
    }
}
