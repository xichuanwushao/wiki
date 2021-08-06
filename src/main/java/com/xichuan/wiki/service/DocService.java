package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.Doc;
import com.xichuan.wiki.domain.DocExample;
import com.xichuan.wiki.mapper.DocMapper;
import com.xichuan.wiki.req.DocQueryReq;
import com.xichuan.wiki.req.DocSaveReq;
import com.xichuan.wiki.resp.DocQueryResp;
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
public class DocService {
    @Resource
    private DocMapper docMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(DocService.class);
    /***
     * 列表CopyUtil复制
     * @param docQueryReq
     * @return
     */
    public List<DocQueryResp> all(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        return respList;
    }


    /***
     * 列表CopyUtil复制
     * @param docQueryReq
     * @return
     */
    public PageResp<DocQueryResp> list(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");

        if(!ObjectUtils.isEmpty(docQueryReq.getName())){
            criteria.andNameLike("%"+ docQueryReq.getName()+"%");
        }

        if(docQueryReq.getPage()!=0 && docQueryReq.getSize()!=0) {
            PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
        }
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        log.info("总行数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /***
     * 保存接口
     * @param docSaveReq
     */
    public void save(DocSaveReq docSaveReq) {
        Doc doc = CopyUtil.copy(docSaveReq,Doc.class);
        if(ObjectUtils.isEmpty(doc.getId())){
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }else{
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }
}
