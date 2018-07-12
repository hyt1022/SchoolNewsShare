package com.nowcoder.toutiao.DAO;


import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELD = "from_id, to_id,content, has_read, conversation_id, created_date, status";
    String SELECT_FIELD = "id," + INSERT_FIELD;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELD, ") " +
            "values(#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate},#{status})"})
    int addMessage(Message message);

    @Select({"select ", INSERT_FIELD, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);


    @Select({"select count(id) from",TABLE_NAME,"where to_id=#{userId} and has_read= 0 and conversation_id=#{conversationId}" })
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select",SELECT_FIELD,"from",TABLE_NAME,"where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetails(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Update({"update",TABLE_NAME,"set status = #{status} where id=#{id}"})
    void deleteMessage(@Param("id") int id,@Param("status") int status);
}
