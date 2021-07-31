package com.xichuan.wiki.controller;

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

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param ebookReq
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(EbookReq ebookReq) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.list( ebookReq);
        resp.setContent(list);
        return resp;
    }
}
