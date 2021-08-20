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