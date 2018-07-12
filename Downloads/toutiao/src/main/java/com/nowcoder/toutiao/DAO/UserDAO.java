package com.nowcoder.toutiao.DAO;

import com.nowcoder.toutiao.model.User;
import org.apache.ibatis.annotations.*;

import javax.annotation.PreDestroy;

@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url , username, fans_count, follow_count, sex, age, intro, school, official";
    String SELECT_FIELDS = " id, name, password, salt, head_url , username, fans_count, follow_count, sex, age, intro, school, official";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl},#{username},#{fansCount},#{followCount},#{sex},#{age},#{intro},#{school},#{official})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

    @Update({"update ", TABLE_NAME, " set follow_count=#{followCount} where id=#{id}"})
    void updateFollowCount(@Param("id") int id,@Param("followCount") int followCount);

    @Update({"update ", TABLE_NAME, " set fans_count=#{fansCount} where id=#{id}"})
    void updateFansCount(@Param("id") int id,@Param("fansCount") int fansCount);

    @Update({"update ", TABLE_NAME, " set head_url=#{headUrl} where id=#{id}"})
    void updateHeadUrl(@Param("headUrl") String headUrl,@Param("id") int id);



    @Update({"update",TABLE_NAME,"set username=#{username} , sex=#{sex} , age=#{age} , intro=#{intro} , school=#{school} where id=#{id}"})
    void updateUserInfo(@Param("id") int id,@Param("username") String username,@Param("sex") int sex,
                        @Param("age") int age,@Param("intro") String intro, @Param("school") String school);
}
