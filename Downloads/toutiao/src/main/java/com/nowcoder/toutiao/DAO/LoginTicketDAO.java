package com.nowcoder.toutiao.DAO;

import com.nowcoder.toutiao.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELD = "user_id,expired,status,ticket";
    String SELECT_FIELD = "id," + INSERT_FIELD;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELD, ") values(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select",SELECT_FIELD,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}


