package com.ctool.board.dao;

import com.ctool.model.board.Board;
import com.ctool.model.board.Lane;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: Kylinrix
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
    int addLane(Lane lane);

    @Select({"select * from " +tableName+" where board_name=#{boardName}"})
    Lane selectByName(String boardName);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Lane selectById(int id);

    @Select({"select * form "+tableName+" where board_id = #{BoardId} order by created_date ASC "})
    List<Lane> selectByBoardId(int boardId);
}
