package com.xichuan.wiki.job;

import com.xichuan.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger log = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;


    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        log.info("更新电子书的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        log.info("更新电子书下的文档数据结束，耗时：{}毫秒",System.currentTimeMillis()-start);
    }

}
