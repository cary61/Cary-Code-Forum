package top.cary.cary_code.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.entity.unum.TextTypeEnum;

@Data
@NoArgsConstructor
public class SimpleAction {
    private Integer textId;

    private String textType;

    private Integer uid;

    private String actionType;

    public Action toAction() {
        return Action.builder()
                .textId(textId)
                .textType(TextTypeEnum.valueOf(textType.toUpperCase()))
                .uid(uid)
                .actionType(ActionTypeEnum.valueOf(actionType.toUpperCase()))
                .build();
    }
}
