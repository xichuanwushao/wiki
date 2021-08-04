package com.xichuan.wiki.controller;

import com.xichuan.wiki.req.CategoryQueryReq;
import com.xichuan.wiki.req.CategorySaveReq;
import com.xichuan.wiki.resp.CategoryQueryResp;
import com.xichuan.wiki.resp.CommonResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param categoryQueryReq
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryQueryReq) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        resp.setContent(list);
        return resp;
    }

    /***
     * 参数使用实体进行模糊查询 并返回指定类型实体
     * @param categorySaveReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categorySaveReq) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(categorySaveReq);
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
        categoryService.delete(id);
        return resp;
    }
}
