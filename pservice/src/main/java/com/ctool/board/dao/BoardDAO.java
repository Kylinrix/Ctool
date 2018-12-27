package com.ctool.board.dao;

import com.ctool.model.board.Board;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: Kylinrix
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
                    "created_date," +
                    "description ";

    String insertFields = " own_user_id,board_name,created_date,description ";


    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{ownUserId},#{boardName},#{createdDate},#{description})"})
    int addBoard(Board board);

    @Select({"select * from " +tableName+" where board_name=#{boardName}"})
    Board selectByName(String boardName);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Board selectById(int id);

    @Select({"select * form "+tableName+" where own_user_id = #{userId} order by created_date ASC"})
    List<Board> selectByUserId(int userId);
}
