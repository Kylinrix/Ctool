package com.ctool.user.dao;


import com.ctool.model.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 12:20
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Mapper
public interface UserDAO {
    String tableName="user";
    String selectFields = " id,name,password,salt,email,head_url,status";
    String insertFields = " name,password,salt,email, head_url,status";
    // #{} 表示bean中的字段，需要与设置的字段名相同。
    @Insert({"insert into "
            ," user ",
            "(",
            insertFields,
            ") values(#{name},#{password},#{salt},#{email},#{headUrl},#{status})"})
    int addUser(User user);

    @Select({"select * from " +tableName+" where name=#{name}"})
    User selectByName(String name);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    User selectById(int id);

}
