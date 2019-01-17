package com.ctool.board.dao;


import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2018/12/27 15:21
 * @Email: Kylinrix@outlook.com
 * @Description:
 */

@Mapper
public interface CardDAO {

    String tableName=" card ";
    String selectFields =
            " id," +
            "panel_id," +
            "card_name," +
            "card_content," +
            "last_changer," +
            "created_date," +
            "description ";

    String insertFields = " panel_id,card_name,card_content,last_changer,created_date,description ";


    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{panelId},#{cardName},#{cardContent},#{lastChanger},#{createdDate},#{description})"})
//    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
//    @SelectKey(before = false,
//            statementType=StatementType.STATEMENT,
//            statement="SELECT LAST_INSERT_ID() AS id",
//            resultType = int.class,
//            keyProperty = "id",
//            keyColumn = "id")
    int insertCard(Card card);



    @Select({"select * from " +tableName+" where card_name=#{cardName}"})
    Card selectByName(String cardName);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Card selectById(int id);

//    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
//    @Results({
//            @Result(id=true,column="id",property="id"),
//            @Result(column="panel_id",property="panelId"),
//            @Result(column="card_name",property="cardName"),
//            @Result(column="last_changer",property="lastChanger"),
//            @Result(column="created_date",property="createdDate"),
//            @Result(column="description",property="description"),
//            @Result(column="id",property="members",
//                    many=@Many(
//                            select="com.",
//                            fetchType=FetchType.LAZY
//                    )
//            )
//    })
//    Card selectByIdWithMembers(int id);

    @Select({"select * from "+tableName+" where panel_id = #{panelId} order by created_date ASC "})
    List<Card> selectByPanelId(int panelId);

    @Delete({"delete from "+tableName + " where id =#{id}"})
    int deleteById(int id);

    @Update({"update ", tableName, " " +
            " set card_name=#{name} " +
            " last_changer=#{userId} " +
            " card_content=#{cardContent} " +
            " description=#{description} "+
            " where id=#{cardId} "})
    int  updateCard( int cardId, String name,int userId,String cardContent,String description);
}
