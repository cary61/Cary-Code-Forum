package top.cary.cary_code.mapper;

import org.apache.ibatis.annotations.*;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.User;

import java.util.List;

@Mapper
public interface ActionMapper {

    @Insert("""
                INSERT INTO `action` (`text_id`, `text_type`, `uid`, `action_type`) 
                VALUES ( #{textId}, #{textType}, #{uid}, #{actionType} )
            """)
    public boolean add(Action action);

    @Delete("""
                DELETE FROM `action` WHERE text_id = #{textId}
                                       AND text_type = #{textType}
                                       AND uid = #{uid}
                                       AND action_type = #{actionType}
            """)
    public boolean delete(Action action);

    @Select("""
                SELECT * FROM `action` WHERE text_id = #{textId}
                                       AND text_type = #{textType}
                                       AND uid = #{uid}
                                       AND action_type = #{actionType}
            """)
    public Action get(Action action);

    @Select("""
                SELECT * FROM `action` WHERE uid = #{uid}
                                       AND text_type = #{textType}
                                       AND action_type = #{actionType}
                    ORDER BY `uid` DESC
            """)
    public List<Action> getRecent(Integer uid, String textType, String actionType);
}
