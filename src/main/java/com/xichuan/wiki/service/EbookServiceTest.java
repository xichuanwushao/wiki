package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.Ebook;
import com.xichuan.wiki.domain.EbookExample;
import com.xichuan.wiki.mapper.EbookMapper;
import com.xichuan.wiki.req.EbookReq;
import com.xichuan.wiki.resp.EbookResp;
import com.xichuan.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookServiceTest {
    @Resource
    private EbookMapper ebookMapper;

    private static final Logger log = LoggerFactory.getLogger(EbookServiceTest.class);

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

    public List<Ebook> list(String name) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+name+"%");
        return ebookMapper.selectByExample(ebookExample);
    }

    public List<Ebook> list(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ebookReq.getName()+"%");
        return ebookMapper.selectByExample(ebookExample);
    }

    /***
     * 对象普通复制
     * @param ebookReq
     * @return
     */
    public List<EbookResp> listResp(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ebookReq.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>() ;
        for (Ebook ebook :ebookList){
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return respList;
    }

    /***
     * 对象CopyUtil复制
     * @param ebookReq
     * @return
     */
    public List<EbookResp> listResp1(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ebookReq.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>() ;
        for (Ebook ebook :ebookList){
            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
            respList.add(ebookResp);
        }
        return respList;
    }

    /***
     * 列表CopyUtil复制
     * @param ebookReq
     * @return
     */
    public List<EbookResp> listResp2(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(ebookReq.getName())){
            criteria.andNameLike("%"+ebookReq.getName()+"%");
        }
        if(ebookReq.getPage()!=0 && ebookReq.getSize()!=0) {
            PageHelper.startPage(ebookReq.getPage(), ebookReq.getSize());
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        log.info("总行数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);
        return respList;
    }

}
