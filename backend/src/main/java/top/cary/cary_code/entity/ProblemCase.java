package top.cary.cary_code.entity;

import lombok.Data;

@Data
public class ProblemCase {

    private Integer id;

    private Integer problemId;

    private String input;

    private String output;

}
