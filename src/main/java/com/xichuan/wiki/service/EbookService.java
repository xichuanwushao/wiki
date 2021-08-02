package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.Ebook;
import com.xichuan.wiki.domain.EbookExample;
import com.xichuan.wiki.mapper.EbookMapper;
import com.xichuan.wiki.req.EbookQueryReq;
import com.xichuan.wiki.req.EbookSaveReq;
import com.xichuan.wiki.resp.EbookQueryResp;
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
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(EbookService.class);

    /***
     * 列表CopyUtil复制
     * @param ebookQueryReq
     * @return
     */
    public PageResp<EbookQueryResp> list(EbookQueryReq ebookQueryReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(ebookQueryReq.getName())){
            criteria.andNameLike("%"+ ebookQueryReq.getName()+"%");
        }

        if(ebookQueryReq.getPage()!=0 && ebookQueryReq.getSize()!=0) {
            PageHelper.startPage(ebookQueryReq.getPage(), ebookQueryReq.getSize());
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        log.info("总行数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /***
     * 保存接口
     * @param ebookSaveReq
     */
    public void save(EbookSaveReq ebookSaveReq) {
        Ebook ebook = CopyUtil.copy(ebookSaveReq,Ebook.class);
        if(ObjectUtils.isEmpty(ebook.getId())){
            ebook.setId(snowFlake.nextId());
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebookMapper.insert(ebook);
        }else{
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
