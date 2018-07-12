package com.nowcoder.toutiao.DAO;


import com.nowcoder.toutiao.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSET_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id," + INSET_FIELDS;

    @Insert({"insert into", TABLE_NAME,"(",INSET_FIELDS,") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where entity_id = #{entityId} and entity_type = #{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

    @Select({"select count(id) from",TABLE_NAME,"where entity_id = #{entityId} and entity_type = #{entityType} and status='1"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    void deleteComment(@Param("id") int id, @Param("status") int status);

}
