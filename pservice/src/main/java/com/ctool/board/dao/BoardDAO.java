package com.ctool.board.dao;

import com.ctool.model.board.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2018/12/27 15:20
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Mapper
public interface BoardDAO {
    String tableName=" board ";
    String selectFields =
                    " id," +
                    "own_user_id," +
                    "board_name," +
                    "created_date,"+ "authorization,"+
                    "description ";

    String insertFields = " own_user_id,board_name,created_date,authorization,description ";


    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{ownUserId},#{boardName},#{createdDate},#{authorization},#{description})"})
    //@SelectKey(before = false,statement="SELECT LAST_INSERT_ID() AS ID",resultType = int.class,keyProperty = "id",keyColumn = "id")
    int insertBoard(Board board);

    @Select({"select * from " +tableName+" where board_name=#{boardName}"})
    Board selectByName(String boardName);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Board selectById(int id);

    @Select({"select * from "+tableName+" where own_user_id = #{userId} order by created_date ASC"})
    List<Board> selectByUserId(int userId);

    @Delete({"delete from "+tableName + " where id =#{id}"})
    int deleteById(int id);

    @Update({"update ", tableName, " " +
            " set authorization=#{code} " +
            " where id=#{boardId} "})
    int  updateAuthorization(@Param("boardId") int boardId,@Param("code")int code);
}
