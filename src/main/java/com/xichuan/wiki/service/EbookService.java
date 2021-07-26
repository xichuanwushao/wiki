package com.xichuan.wiki.service;

import com.xichuan.wiki.domain.Ebook;
import com.xichuan.wiki.domain.EbookExample;
import com.xichuan.wiki.mapper.EbookMapper;
import com.xichuan.wiki.req.EbookReq;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

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

}
