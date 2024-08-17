package top.cary.cary_code.mapper;

import org.apache.ibatis.annotations.*;
import top.cary.cary_code.entity.Comment;
import top.cary.cary_code.entity.User;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM `comment` WHERE `id` = #{id}")
    public Comment get(Integer id);

    @Select("SELECT * FROM `comment` WHERE `article_id` = #{articleId} ORDER BY `create_time` asc")
    public List<Comment> getCommentsByArticle(Integer articleId);

    @Select("SELECT * FROM `comment`")
    public List<Comment> getAll();

    @Insert("""
                INSERT INTO `comment` (`uid`, `nickname`, `article_id`, `content`, `create_time`)
                    VALUES ( #{uid}, #{nickname}, #{articleId}, #{content}, #{createTime} )
            """)
    public boolean add(Comment comment);

    @Update("UPDATE `comment` SET `likes` = #{likes} WHERE `id` = #{id}")
    public boolean updateLikes(Integer id, Integer likes);

    @Update("UPDATE `comment` SET `hates` = #{hates} WHERE `id` = #{id}")
    public boolean updateHates(Integer id, Integer hates);

    @Delete("DELETE FROM `comment` WHERE `id` = #{id}")
    public boolean delete(Integer id);

    @Delete("DELETE FROM `comment` WHERE `article_id` = #{articleId}")
    public Integer deleteByArticleId(Integer articleId);

    @Delete("DELETE FROM `comment` WHERE `comment_id` = #{commentId}")
    public Integer deleteByCommentId(Integer commentId);

    @Update("UPDATE `comment` SET `nickname` = #{nickname} WHERE `uid` = #{uid}")
    public boolean updateNickname(User user);
}
