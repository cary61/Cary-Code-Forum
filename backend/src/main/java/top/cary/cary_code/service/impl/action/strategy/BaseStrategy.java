package top.cary.cary_code.service.impl.action.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.Text;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.mapper.ArticleMapper;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.utils.Response;

@Component
public class BaseStrategy implements Strategy {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CommentMapper commentMapper;

    @Override
    public ActionTypeEnum getActionType() {
        return null;
    }

    protected Text getText(Action action) {
        Integer textId = action.getTextId();
        return switch (action.getTextType()) {
            case ARTICLE -> articleMapper.get(textId);
            case COMMENT -> commentMapper.get(textId);
        };
    }

    @Override
    public Response apply(Action action) {
        return null;
    }
}
