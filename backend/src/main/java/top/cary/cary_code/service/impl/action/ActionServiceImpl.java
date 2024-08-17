package top.cary.cary_code.service.impl.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.service.ActionService;
import top.cary.cary_code.service.impl.action.strategy.Strategy;
import top.cary.cary_code.utils.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionServiceImpl implements ActionService {
    private Map<ActionTypeEnum, Strategy> strategyMap;

    @Autowired
    public ActionServiceImpl(List<Strategy> strategies) {
        strategyMap = new HashMap<>();
        for (Strategy strategy : strategies) {
            if (strategy.getActionType() != null) {
                strategyMap.put(strategy.getActionType(), strategy);
            }
        }
    }

    @Override
    public Response serve(Action action) {
        return strategyMap.get(action.getActionType()).apply(action);
    }
}
