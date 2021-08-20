package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.Content;
import com.xichuan.wiki.domain.Doc;
import com.xichuan.wiki.domain.DocExample;
import com.xichuan.wiki.exception.BusinessException;
import com.xichuan.wiki.exception.BusinessExceptionCode;
import com.xichuan.wiki.mapper.ContentMapper;
import com.xichuan.wiki.mapper.DocMapper;
import com.xichuan.wiki.mapper.DocMapperCust;
import com.xichuan.wiki.req.DocQueryReq;
import com.xichuan.wiki.req.DocSaveReq;
import com.xichuan.wiki.resp.DocQueryResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.util.CopyUtil;
import com.xichuan.wiki.util.RedisUtil;
import com.xichuan.wiki.util.RequestContext;
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
    private ContentMapper contentMapper;
    @Resource
    private DocMapperCust docMapperCust;
    @Resource
    private SnowFlake snowFlake;
    @Resource
    private RedisUtil redisUtil;


    private static final Logger log = LoggerFactory.getLogger(DocService.class);
    /***
     * 列表CopyUtil复制
     * @param ebookId
     * @return
     */
    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        //列表复制
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
        Content content = CopyUtil.copy(docSaveReq,Content.class);
        if(ObjectUtils.isEmpty(doc.getId())){
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count<1){
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increateViewCount(id);
        if(ObjectUtils.isEmpty(content)){
            return "";
        }else{
            return content.getContent();
        }
    }

    /***
     * 点赞
     * @param id
     */
    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
