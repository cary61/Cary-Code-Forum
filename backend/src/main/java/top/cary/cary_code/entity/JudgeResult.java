package top.cary.cary_code.entity;

import lombok.Builder;
import lombok.Data;
import top.cary.cary_code.entity.unum.JudgeStatusEnum;

import java.util.List;

@Data
@Builder
public class JudgeResult {

    private Integer id;

    private Long serialId;

    private Integer uid;

    private JudgeStatusEnum status;

    private Long timeMillis;

    private Long memory;

    private List<JudgeTaskResult> caseResult;
}
