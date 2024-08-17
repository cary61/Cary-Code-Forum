package top.cary.cary_code.mapper;

import org.apache.ibatis.annotations.*;
import top.cary.cary_code.entity.Article;
import top.cary.cary_code.entity.User;
import top.cary.cary_code.utils.Response;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("SELECT * FROM `article` WHERE `id` = #{id}")
    public Article get(Integer id);

    @Select("SELECT * FROM `article` ORDER BY `update_time` desc LIMIT #{k}")
    public List<Article> getTop(int k);

    @Select("SELECT * FROM `article`")
    public List<Article> getAll();

    @Select("SELECT * FROM `article` WHERE `uid` = #{uid}")
    public List<Article> getListByUid(Integer uid);

    @Select("SELECT * FROM `article` ORDER BY `likes` DESC LIMIT #{k}")
    public List<Article> getHottestArticles(Integer k);

    @Insert("""
                INSERT INTO `article` (`uid`, `nickname`, `title`, `content`, `type`, `create_time`, `update_time`, `latest_operate_time`)
                             VALUES ( #{uid}, #{nickname}, #{title}, #{content}, #{type}, #{createTime}, #{updateTime}, #{latestOperateTime})
            """)
    public boolean add(Article article);

    @Update("""
                UPDATE `article` 
                    SET `title` = #{title},
                        `content` = #{content},
                        `type` = #{type},
                        `update_time` = #{updateTime},
                        `latest_operate_time` = #{latestOperateTime}
                WHERE `id` = #{id}
            """)
    public boolean update(Article article);

    @Update("UPDATE `article` SET `content` = #{content} WHERE `id` = #{id}")
    public boolean updateContent(Integer id, String content);

    @Update("UPDATE `article` SET `likes` = #{likes} WHERE `id` = #{id}")
    public boolean updateLikes(Integer id, Integer likes);

    @Update("UPDATE `article` SET `hates` = #{hates} WHERE `id` = #{id}")
    public boolean updateHates(Integer id, Integer hates);

    @Delete("DELETE FROM `article` WHERE `id` = #{id}")
    public boolean delete(Integer id);

    @Update("UPDATE `article` SET `nickname` = #{nickname} WHERE `uid` = #{uid}")
    public boolean updateNickname(User user);

    @Update("UPDATE `article` SET `latest_operate_time` = #{time} WHERE `id` = #{id}")
    public boolean updateLatestOperateTime(Integer id, LocalDateTime time);

    @Update("UPDATE `article` SET `comments_amount` = #{commentsAmount} WHERE `id` = #{id}")
    public boolean updateCommentsAmount(Integer id, Integer commentsAmount);
}
