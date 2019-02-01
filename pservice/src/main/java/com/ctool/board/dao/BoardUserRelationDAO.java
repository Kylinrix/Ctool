package com.ctool.board.dao;

import com.ctool.model.BoardUserRelation;
import com.ctool.model.board.Board;
import com.ctool.model.user.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/8 16:44
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Mapper
public interface BoardUserRelationDAO {
    String tableName=" board_user_relation ";
    String selectFields = " id," +
                    "board_id," +
                    "user_id," +
                    "user_role,"+
                    "created_date "+
                    "description";

    String insertFields = " board_id,user_id,user_role,created_date,description ";

    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{boardId},#{userId},#{userRole},#{createdDate},#{description})"})
    int insertBoardUserRelation(BoardUserRelation boardUserRelation);


    //此处要保证User与Board同用一个数据库，等值联结
    @Select({"select u.id,u.name,u.password,u.salt,u.email,u.head_url,u.status from "+
            tableName+"as bu ,user as u where u.id = bu.user_id and bu.board_id = #{boardId} order by bu.created_date DESC"})
    List<User> selectUsersByBoardId(int boardId);

    //等值联结
    @Select({"select b.id,b.own_user_id,b.board_name,b.created_date,b.authorization,b.description " +
            "from "+tableName+"as bu,board as b where b.id = bu.board_id and bu.user_id = #{userId} order by bu.created_date DESC "})
    List<Board> selectBoardsByUserId(int userId);

    @Select({"select * from "+tableName+" where board_id = #{boardId} and user_id = #{userId} and user_role != 2 order by created_date DESC "})
    BoardUserRelation selectBoardAndUserRelation(@Param("boardId") int boardId, @Param("userId") int userId);

    @Delete({"delete from "+tableName+" where board_id = #{boardId} and user_id = #{userId}"})
    int deleteBoardAndUserRelation(@Param("boardId") int boardId, @Param("userId") int userId);

}
