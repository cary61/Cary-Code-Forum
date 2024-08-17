package top.cary.cary_code.entity;

import lombok.Data;

@Data
public class Case {

    Integer id;

    String input;

    String output;

    Integer maxTime;

    Long maxMemory;
}
