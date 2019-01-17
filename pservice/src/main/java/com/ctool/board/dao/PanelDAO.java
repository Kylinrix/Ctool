package com.ctool.board.dao;

import com.ctool.model.board.Lane;
import com.ctool.model.board.Panel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/15 01:21
 * @Email: Kylinrix@outlook.com
 * @Description:
 */

@Mapper
public interface PanelDAO {
    String tableName=" panel ";

    String selectFields =
            " id," +
                    "lane_id," +
                    "panel_name," +
                    "created_date," +
                    "description ";

    String insertFields = " lane_id,panel_name,created_date,description ";

    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{laneId},#{PanelName},#{createdDate},#{description})"})
        //@SelectKey(before = false,statementType=StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id",resultType = int.class,keyProperty = "id",keyColumn = "id")
    int insertPanel(Panel panel);


    @Select({"select * from " +tableName+" where board_name=#{boardName}"})
    Lane selectByName(String panel);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Panel selectById(int id);

    @Select({"select * form "+tableName+" where lane_id = #{laneId} order by created_date ASC "})
    List<Lane> selectByLaneId(int laneId);

    @Delete({"delete from "+tableName + " where id =#{id}"})
    int deleteById(int id);

    @Update({"update ", tableName, " " +
            " set panel_name=#{name} " +
            " description=#{description} "+
            " where id=#{panelId} "})
    int  updatePanel( int panelId, String name,String description);


}
