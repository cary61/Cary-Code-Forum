package top.cary.cary_code.service.impl.action.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.Action;
import top.cary.cary_code.entity.Text;
import top.cary.cary_code.entity.unum.ActionTypeEnum;
import top.cary.cary_code.mapper.ActionMapper;
import top.cary.cary_code.mapper.ArticleMapper;
import top.cary.cary_code.mapper.CommentMapper;
import top.cary.cary_code.utils.Response;

@Component
public class LikeStrategy extends BaseStrategy {

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ActionTypeEnum getActionType() {
        return ActionTypeEnum.LIKE;
    }

    @Override
    public Response apply(Action action) {
        Text text = getText(action);
        if (text == null) {
            return Response.failWithMsg("这则消息已不存在");
        }
        try {
            actionMapper.add(action);
        } catch (Exception e) {
            return Response.failWithMsg("已经赞过了");
        }
        Integer textId = text.getId();
        int newLikes = text.getLikes() + 1;
        switch (action.getTextType()) {
            case ARTICLE -> articleMapper.updateLikes(textId, newLikes);
            case COMMENT -> commentMapper.updateLikes(textId, newLikes);
        }
        return Response.ok();
    }
}
