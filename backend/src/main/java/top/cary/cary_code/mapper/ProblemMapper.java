package top.cary.cary_code.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary.cary_code.entity.Problem;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Select("SELECT * FROM `problem` WHERE `id` = #{id} ")
    Problem get(int id);

    @Select("SELECT * FROM `problem`")
    List<Problem> getAll();

    @Select("SELECT * FROM `problem` WHERE `name` = #{name} ")
    Problem getByName(String name);

    @Delete("DELETE FROM `problem` WHERE `id` = #{id} ")
    boolean delete(Integer id);

    @Insert("""
                INSERT INTO `problem`
                        (`name`, `background`, `description`, `input_format`, `output_format`, `tip`, `difficulty`)
                    VALUES
                        (#{name}, #{background}, #{description},
                                #{inputFormat}, #{outputFormat}, #{tip}, #{difficulty})
            """)
    boolean add(Problem problem);

    @Insert("""
                INSERT INTO `problem_case`
                        (`problem_id`, `input`, `output`, `max_time`, `max_memory`)
                    VALUES
                        (#{problemId}, #{input}, #{output}, #{maxTime}, #{maxMemory})
            """)
    boolean addCase(Integer problemId, String input, String output, Integer maxTime, Long maxMemory);
}
