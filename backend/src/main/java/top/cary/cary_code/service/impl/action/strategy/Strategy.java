package top.cary.cary_code.service.impl.action.strategy;

import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.utils.Response;

import java.util.function.Function;

public interface Strategy extends Function<Action, Response> {

    ActionTypeEnum getActionType();

}
