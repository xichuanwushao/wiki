#----统计某一个电子书数据
select ebook_id ,count(1) doc_count, sum(view_count) view_count, sum(vote_count) vote_count
from doc where  ebook_id = 1;
#----按电子书分组统计文档数据
select ebook_id ,count(1) doc_count, sum(view_count) view_count, sum(vote_count) vote_count
from doc group by ebook_id;
#----按电子书分组统计文档数据 并更新到对应的电子书中
update ebook t1, ( select ebook_id, count(1) doc_count, sum(view_count) view_count, sum(vote_count)
    vote_count from doc group by ebook_id ) t2
set t1.doc_count = t2.doc_count , t1.view_count = t2.view_count , t1.vote_count = t2.vote_count
where t1.id = t2.ebook_id ;


-- 方案一（ID不连续）
--     删除今天的数据
--     为所有的电子数生成一条今天的纪录
--     根新总阅读数 总点赞数
--     更新今日阅读数 今日点赞数
-- 方案二（ID连续）
--     为所有的电子书生成一条今天的记录 如果还没有
--     更新总阅读数 总点赞数
--     更新今日阅读数 今日点赞数
insert into ebook_snapshot(ebook_id, `date`, view_count, vote_count, view_increase, vote_increase)
select t1.id, curdate(), 0, 0, 0, 0
from ebook t1
where not exists(select 1 from ebook_snapshot t2
                 where t1.id = t2.ebook_id
                   and t2.`date` = current_date());

update ebook_snapshot t1, ebook t2
set t1.view_count = t2.view_count, t1.vote_count = t2.vote_count
where t1.`date` = curdate()
and t1.ebook_id = t2.id;

-- 获取昨天数据
select t1.ebook_id, view_count , vote_count from ebook_snapshot t1
where t1.`date` = date_sub(curdate(),interval 1 day);

# 用今天的数据和昨天的数据做比较 昨天的数据就是上方生成的几何 今天的数据查ebook_snapshot表就可以了 只能更新昨天有数据的记录  不论昨天那有没有数据都要跟新 昨天如果没有数据就把昨天数据设置成0
update ebook_snapshot t1 left join  (select ebook_id, view_count , vote_count from ebook_snapshot
                                     where `date` = date_sub(curdate(),interval 1 day) ) t2
    on t1.ebook_id = t2.ebook_id
set t1.view_increase = (t1.view_count - ifnull(t2.view_count,0)),
    t1.vote_increase = (t1.vote_count - ifnull(t2.vote_count,0))
where  t1.`date` = curdate();
