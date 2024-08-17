package top.cary.cary_code.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary.cary_code.entity.ProblemCase;

import java.util.List;

@Mapper
public interface ProblemCaseMapper {

    @Select("SELECT * FROM `problem_case` WHERE `problem_id` = #{problemId} ")
    List<ProblemCase> getByProblemId(int problemId);
}
