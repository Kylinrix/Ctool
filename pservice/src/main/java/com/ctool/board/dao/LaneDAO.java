package com.ctool.board.dao;

import com.ctool.model.board.Board;
import com.ctool.model.board.Lane;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2018/12/27 15:21
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Mapper
public interface LaneDAO {
    String tableName=" lane ";
    String selectFields =
                    " id," +
                    "board_id," +
                    "lane_name," +
                    "created_date," +
                    "description ";

    String insertFields = " board_id,lane_name,created_date,description ";

    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{boardId},#{laneName},#{createdDate},#{description})"})
    //@SelectKey(before = false,statementType=StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id",resultType = int.class,keyProperty = "id",keyColumn = "id")
    int insertLane(Lane lane);

    @Select({"select * from " +tableName+" where lane_name=#{laneName}"})
    Lane selectByName(String laneName);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Lane selectById(int id);

    @Select({"select * form "+tableName+" where board_id = #{boardId} order by created_date ASC "})
    List<Lane> selectByBoardId(int boardId);

    @Delete({"delete from "+tableName + " where id =#{id}"})
    int deleteById(int id);
}
