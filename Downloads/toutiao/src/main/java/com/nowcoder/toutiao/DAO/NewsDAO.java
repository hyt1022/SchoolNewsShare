package com.nowcoder.toutiao.DAO;


import com.nowcoder.toutiao.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id, official, status, content";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId},#{Official},#{status},#{content})"})
    int addNews(News news);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);


    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where status=1 order by created_date desc limit #{offset},#{limit}"})
    List<News> selectByOffset(@Param("offset") int offset, @Param("limit") int limit);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where official=#{Official} and status=1 order by created_date desc limit #{offset},#{limit}"})
    List<News> selectByOfficialAndOffset(@Param("Official") int Official,@Param("offset") int offset, @Param("limit") int limit);


    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where user_id=#{userId} and status=1 order by id DESC limit #{offset},#{limit}"})
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);

    @Update({"update",TABLE_NAME,"set status = #{status} where id=#{id}"})
    void deleteNews(@Param("id") int id,@Param("status") int status);

}
