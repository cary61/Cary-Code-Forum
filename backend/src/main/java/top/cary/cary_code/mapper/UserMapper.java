package top.cary.cary_code.mapper;

import org.apache.ibatis.annotations.*;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.entity.unum.AuthorityEnum;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper
public interface UserMapper {

    @Insert("""
        INSERT INTO `user`(name, password_hash, nickname, avatar, signature, authority, register_time) 
            VALUES( #{name}, #{passwordHash}, #{nickname}, #{avatar}, #{signature}, #{authority}, #{registerTime} )
    """)
    public boolean add(String name,
                       String passwordHash,
                       String nickname,
                       String avatar,
                       String signature,
                       AuthorityEnum authority,
                       LocalDateTime registerTime);

//    @Insert("INSERT INTO `user`(name, password_hash, authority) VALUES( #{name}, #{passwordHash}, #{authority} )")
//    public boolean add(User user);

    @Select("SELECT * FROM `user` WHERE `uid` = #{uid}")
    public User get(Integer uid);

    @Select("SELECT * FROM `user` WHERE `name` = #{name}")
    public User getByName(String name);

    @Select("SELECT * FROM `user` WHERE `nickname` = #{nickname}")
    public User getByNickname(String nickname);

    @Update("""
                UPDATE `user`
                    SET
                        `nickname` = #{nickname},
                        `avatar` = #{avatar},
                        `signature` = #{signature}
                WHERE `uid` = #{uid}
            """)
    public boolean update(User user);

    @Delete("DELETE FROM `user` WHERE `uid` = #{uid}")
    public boolean delete(Integer uid);

    //STR_TO_DATE(#{banUntil}, '%Y-%c-%d %H:%i:%s')
    @Update("""
                UPDATE `user` 
                    SET `banned_until` = STR_TO_DATE(#{banUntil}, '%Y-%m-%d %H:%i:%s')
                    WHERE `uid` = ${uid}
            """)
    public boolean ban(Integer uid, String banUntil);
}
