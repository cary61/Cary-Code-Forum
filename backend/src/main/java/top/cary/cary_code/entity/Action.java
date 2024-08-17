package top.cary.cary_code.entity;

import lombok.Builder;
import lombok.Data;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.entity.unum.TextTypeEnum;

@Data
@Builder
public class Action {

    private Integer id;

    private Integer textId;

    private TextTypeEnum textType;

    private Integer uid;

    private ActionTypeEnum actionType;
}
