package com.nowcoder.toutiao.DAO;


import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.model.Relation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface RelationDAO {
    String TABLE_NAME = "relation";
    String INSERT_FIELDS = " from_id, to_id, relation_type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "values (#{fromId},#{toId},#{relationType})"})
    int addRelation(Relation relation);

    @Delete({"delete from ", TABLE_NAME, " where from_id=#{fromId} and to_id=#{toId}"})
    void deleteByFromAndToID(@Param("fromId") int fromId,@Param("toId") int toId);

    @Select({"select count(to_id) from",TABLE_NAME,"where to_id=#{toId}"})
    int getUserFansCount(@Param("toId") int toId);

    @Select({"select count(from_id) from",TABLE_NAME,"where from_id=#{fromId}"})
    int getUserFollowCount(@Param("fromId") int fromId);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where from_id=#{userId}"})
    List<Relation> getRelationByFromId(@Param("userId") int userId);
}
