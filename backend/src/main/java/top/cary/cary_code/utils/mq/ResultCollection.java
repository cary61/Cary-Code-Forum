package top.cary.cary_code.utils.mq;

import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.JudgeResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResultCollection {
    Map<Long,JudgeResult> results = new ConcurrentHashMap<>();

    public JudgeResult get(Long serialId) {
        return results.get(serialId);  // TO-DO
    }

    public void put(Long serialId, JudgeResult judgeResult) {
        results.put(serialId, judgeResult);
    }
}
