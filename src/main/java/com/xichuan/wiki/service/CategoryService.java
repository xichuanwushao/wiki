package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.Category;
import com.xichuan.wiki.domain.CategoryExample;
import com.xichuan.wiki.mapper.CategoryMapper;
import com.xichuan.wiki.req.CategoryQueryReq;
import com.xichuan.wiki.req.CategorySaveReq;
import com.xichuan.wiki.resp.CategoryQueryResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.util.CopyUtil;
import com.xichuan.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    /***
     * 列表CopyUtil复制
     * @param categoryQueryReq
     * @return
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryQueryReq) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        if(!ObjectUtils.isEmpty(categoryQueryReq.getName())){
            criteria.andNameLike("%"+ categoryQueryReq.getName()+"%");
        }

        if(categoryQueryReq.getPage()!=0 && categoryQueryReq.getSize()!=0) {
            PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
        }
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        log.info("总行数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /***
     * 保存接口
     * @param categorySaveReq
     */
    public void save(CategorySaveReq categorySaveReq) {
        Category category = CopyUtil.copy(categorySaveReq,Category.class);
        if(ObjectUtils.isEmpty(category.getId())){
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
