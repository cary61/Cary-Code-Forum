package top.cary.cary_code.entity;

import lombok.Data;
import top.cary.cary_code.entity.unum.LanguageEnum;

@Data
public class CodeSubmission {

    private String code;

    private LanguageEnum language;

    private Integer uid;

    private Integer problemId;
}
