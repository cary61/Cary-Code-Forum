package top.cary.cary_code.utils.onlinejudge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.CodeSubmission;
import top.cary.cary_code.entity.JudgeResult;
import top.cary.cary_code.entity.JudgeTask;
import top.cary.cary_code.entity.JudgeTaskResult;
import top.cary.cary_code.entity.unum.JudgeStatusEnum;
import top.cary.cary_code.entity.unum.LanguageEnum;
import top.cary.cary_code.service.JudgeService;
import top.cary.cary_code.utils.mq.Message;
import top.cary.cary_code.utils.mq.MessageQueue;
import top.cary.cary_code.utils.mq.ResultCollection;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class JudgeTaskManager {

    private static final Queue<JudgeTask> EMPTY_QUEUE = new ArrayDeque<>();

    @Autowired
    JudgeService judgeService;

    @Autowired
    MessageQueue messageQueue;
    @Autowired
    ResultCollection resultCollection;

    private boolean messageLoaded = false;
    private long messageId;

    private int uid;

    private Queue<JudgeTask> queue = EMPTY_QUEUE;
    private int taskAmount;
    private List<JudgeTaskResult> resultList;

    public synchronized JudgeTask requestJudgeTask() {
        if (!messageLoaded) {
            tryLoadMessage();
        }
        JudgeTask judgeTask = queue.poll();
//        if (judgeTask != null) {
//            System.out.println(judgeTask);
//        }
        return judgeTask;
    }

    public synchronized void uploadResult(JudgeTaskResult judgeTaskResult) {
        resultList.add(judgeTaskResult);
        System.out.println(judgeTaskResult);
        taskAmount--;
        if (taskAmount == 0) {
            analyseAndSubmitResult();
            messageLoaded = false;
            tryLoadMessage();
        }
    }

    private void analyseAndSubmitResult() {
        JudgeStatusEnum judgeStatus = JudgeStatusEnum.AC;
        long timeMillis = 0;
        long memory = 0;
        // calculate
        for (JudgeTaskResult result : resultList) {
            if (judgeStatus.equals(JudgeStatusEnum.AC)
                    && !result.getStatus().equals(JudgeStatusEnum.AC.name())) {
                judgeStatus = JudgeStatusEnum.valueOf(result.getStatus());
            }
            timeMillis += result.getTimeMillis();
            memory += result.getMemory();
        }
        // build value object
        Collections.sort(resultList, Comparator.comparingInt(JudgeTaskResult::getTaskId));
        JudgeResult judgeResult = JudgeResult.builder()
                .serialId(messageId)
                .uid(uid)
                .status(judgeStatus)
                .timeMillis(timeMillis)
                .memory(memory)
                .caseResult(resultList)
                .build();
        // upload whole result
        resultCollection.put(messageId, judgeResult);
        System.out.println("结果： " + judgeResult.toString());
        resultList = null;
    }

    private void tryLoadMessage() {
        Message message = messageQueue.pollByTopic("JUDGE");
        if (message == null) {
            messageLoaded = false;
            return;
        }
        messageLoaded = true;
        messageId = message.getId();
//        uid = ((CodeSubmission)message.getData()).getUid();
        System.out.println(message);
        queue = judgeService.getJudgeTaskQueue(message);
        taskAmount = queue.size();
        resultList = new ArrayList<>();
    }

}
