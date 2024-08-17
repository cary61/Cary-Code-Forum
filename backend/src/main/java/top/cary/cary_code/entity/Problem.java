package top.cary.cary_code.entity;

import lombok.Data;

@Data
public class Problem {

    private Integer id;

    private String name;

    private String background;

    private String description;

    private String inputFormat;

    private String outputFormat;

    private String tip;

    private String difficulty;
}
