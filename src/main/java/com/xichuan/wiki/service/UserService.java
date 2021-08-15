package com.xichuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xichuan.wiki.domain.User;
import com.xichuan.wiki.domain.UserExample;
import com.xichuan.wiki.exception.BusinessException;
import com.xichuan.wiki.exception.BusinessExceptionCode;
import com.xichuan.wiki.mapper.UserMapper;
import com.xichuan.wiki.req.UserQueryReq;
import com.xichuan.wiki.req.UserSaveReq;
import com.xichuan.wiki.resp.UserQueryResp;
import com.xichuan.wiki.resp.PageResp;
import com.xichuan.wiki.util.CopyUtil;
import com.xichuan.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /***
     * 列表CopyUtil复制
     * @param userQueryReq
     * @return
     */
    public PageResp<UserQueryResp> list(UserQueryReq userQueryReq) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if(!ObjectUtils.isEmpty(userQueryReq.getLoginName())){
            criteria.andLoginNameEqualTo( userQueryReq.getLoginName());
        }
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        log.info("总行数:{}",pageInfo.getTotal());
        log.info("总页数:{}",pageInfo.getPages());
        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /***
     * 保存接口
     * @param userSaveReq
     */
    public void save(UserSaveReq userSaveReq) {
        User user = CopyUtil.copy(userSaveReq,User.class);
        if(ObjectUtils.isEmpty(user.getId())){
            User userDB = selectByLoginName(user.getLoginName());
            if(!ObjectUtils.isEmpty(userDB)){
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else {
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{
            // 更新
            user.setLoginName(null);
            //如果这个user里面属性有值我才去更新 没有值我就不用去跟新这个字段
            userMapper.updateByPrimaryKeySelective(user);
        }
    }
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if(!ObjectUtils.isEmpty(loginName)){
            criteria.andLoginNameEqualTo( loginName);
        }
        List<User> userList  = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }else {
            return userList.get(0);
        }
    }
}
