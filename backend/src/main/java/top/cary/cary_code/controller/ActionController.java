package top.cary.cary_code.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.cary.cary_code.entity.dto.SimpleAction;
import top.cary.cary_code.service.ActionService;
import top.cary.cary_code.utils.Response;

@RestController
@RequestMapping("/action")
public class ActionController {

    @Autowired
    ActionService actionService;

    @PostMapping("/act")
    public Response act(@RequestBody SimpleAction simpleAction,
                        HttpServletRequest httpServletRequest) {
        return actionService.serve(simpleAction.toAction());
    }

    @PostMapping("/like")
    public Response like(@RequestBody SimpleAction simpleAction,
                         HttpServletRequest httpServletRequest) {
        simpleAction.setActionType("LIKE");
        return actionService.serve(simpleAction.toAction());
//        return actionService.serve(Action.builder()
//                .textId(textId)
//                .textType(TextTypeEnum.valueOf(textType.toUpperCase()))
//                .uid(uid)
//                .actionType(ActionTypeEnum.LIKE)
//                .build());
    }

    @PostMapping("/hate")
    public Response hate(@RequestBody SimpleAction simpleAction,
                         HttpServletRequest httpServletRequest) {
        simpleAction.setActionType("HATE");
        return actionService.serve(simpleAction.toAction());
    }
}
