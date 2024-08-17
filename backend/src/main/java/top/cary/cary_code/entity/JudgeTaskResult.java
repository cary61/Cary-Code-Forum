package top.cary.cary_code.entity;

import lombok.Data;

@Data
public class JudgeTaskResult {
    private Long messageId;

    private Integer taskId;

    private String status;

    private Integer timeMillis;

    private Long memory;
}
