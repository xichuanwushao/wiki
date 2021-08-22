package com.xichuan.wiki.mapper;

import com.xichuan.wiki.domain.Test;
import com.xichuan.wiki.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {

    public void genSnapshot();

    List<StatisticResp> getStatistic();
}
