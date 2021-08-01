package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.Ebook;
import com.xichuan.wiki.domain.EbookExample;
import com.xichuan.wiki.mapper.EbookMapper;
import com.xichuan.wiki.req.EbookQueryReq;
import com.xichuan.wiki.resp.EbookQueryResp;
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

    public List<Ebook> list(EbookQueryReq ebookQueryReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ ebookQueryReq.getName()+"%");
        return ebookMapper.selectByExample(ebookExample);
    }

    /***
     * 对象普通复制
     * @param ebookQueryReq
     * @return
     */
    public List<EbookQueryResp> listResp(EbookQueryReq ebookQueryReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ ebookQueryReq.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookQueryResp> respList = new ArrayList<>() ;
        for (Ebook ebook :ebookList){
            EbookQueryResp ebookQueryResp = new EbookQueryResp();
            BeanUtils.copyProperties(ebook, ebookQueryResp);
            respList.add(ebookQueryResp);
        }
        return respList;
    }

    /***
     * 对象CopyUtil复制
     * @param ebookQueryReq
     * @return
     */
    public List<EbookQueryResp> listResp1(EbookQueryReq ebookQueryReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+ ebookQueryReq.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookQueryResp> respList = new ArrayList<>() ;
        for (Ebook ebook :ebookList){
            EbookQueryResp ebookQueryResp = CopyUtil.copy(ebook, EbookQueryResp.class);
            respList.add(ebookQueryResp);
        }
        return respList;
    }

    /***
     * 列表CopyUtil复制
     * @param ebookQueryReq
     * @return
     */
    public List<EbookQueryResp> listResp2(EbookQueryReq ebookQueryReq) {
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
        return respList;
    }

}
