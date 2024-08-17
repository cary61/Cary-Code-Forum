package top.cary.cary_code.entity;

import lombok.Data;

import java.util.List;

@Data
public class CaseDTO {

    Integer problemId;

    List<Case> cases;
}
