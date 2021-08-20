package com.xichuan.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {

    public void increateViewCount(@Param("id") Long id);

}
