package top.cary.cary_code.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JudgeTask {

    private Long messageId;

    private int taskId;

    private CodeSubmission codeSubmission;

    private String caseInput;

    private String caseOutput;
}
