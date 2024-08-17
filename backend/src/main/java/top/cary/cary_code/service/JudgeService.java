package top.cary.cary_code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cary.cary_code.entity.*;
import top.cary.cary_code.entity.unum.PollingStatusEnum;
import top.cary.cary_code.mapper.ProblemCaseMapper;
import top.cary.cary_code.mapper.ProblemMapper;
import top.cary.cary_code.utils.Response;
import top.cary.cary_code.utils.mq.Message;
import top.cary.cary_code.utils.mq.MessageQueue;
import top.cary.cary_code.utils.mq.ResultCollection;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Service
public class JudgeService {

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    ProblemCaseMapper problemCaseMapper;

    @Autowired
    MessageQueue messageQueue;

    @Autowired
    ResultCollection resultCollection;

    private final long DEFAULT_TIME_INTERVAL = 1000;

    private final long DEFAULT_FIRST_TIME_INTERVAL = 1000;

    public Response judge(CodeSubmission codeSubmission) {
        long serialId = messageQueue.offer("JUDGE", codeSubmission);
        return Response.ok(PollingMessage.builder()
                .pollingId(Long.toString(serialId))
                .pollingStatus(PollingStatusEnum.NOT_YET)
                .timeIntervalMillis(DEFAULT_FIRST_TIME_INTERVAL)
                .build());
    }

    public Response getProblemList() {
        return Response.ok(problemMapper.getAll());
    }

    public Response getProblem(Integer problemId) {
        if (problemId == null) {
            return Response.failWithMsg("未传入problem_id");
        }
        return Response.ok(problemMapper.get(problemId));
    }

    public Response getProblemByName(String name) {
        return Response.ok(problemMapper.getByName(name));
    }

    public Response deleteProblem(Integer id) {
        return Response.resultOf(problemMapper.delete(id));
    }

    public Response addProblem(Problem problem) {
        return Response.resultOf(problemMapper.add(problem));
    }

    public Response addCase(CaseDTO caseDTO) {
        Integer problemId = caseDTO.getProblemId();
        boolean ok = true;
        for (Case cas : caseDTO.getCases()) {
            problemMapper.addCase(problemId, cas.getInput(), cas.getOutput(), cas.getMaxTime(), cas.getMaxMemory());
        }
        return Response.resultOf(ok);
    }

    public Response getResult(Long pollingId) {
        if (pollingId == null) {
            return Response.failWithMsg("请求无效");
        }
        JudgeResult judgeResult = resultCollection.get(pollingId);
        System.out.println("获得了：" + judgeResult);
        PollingMessage pollingMessage = PollingMessage.builder().pollingId(Long.toString(pollingId)).build();
        if (judgeResult == null) {
            pollingMessage.setPollingStatus(PollingStatusEnum.NOT_YET);
            pollingMessage.setTimeIntervalMillis(DEFAULT_TIME_INTERVAL);
        } else {
            pollingMessage.setPollingStatus(PollingStatusEnum.READY);
            pollingMessage.setData(judgeResult);
        }
        return Response.ok(pollingMessage);
    }

    public Queue<JudgeTask> getJudgeTaskQueue(Message message) {
        CodeSubmission codeSubmission = (CodeSubmission) message.getData();
        System.out.println(codeSubmission);
        int problemId = codeSubmission.getProblemId();
        List<ProblemCase> problemCaseList = problemCaseMapper.getByProblemId(problemId);
        Queue<JudgeTask> queue = new ArrayDeque<>();
        int taskId = 1;
        for (ProblemCase problemCase : problemCaseList) {
            JudgeTask judgeTask = JudgeTask.builder()
                    .messageId(message.getId())
                    .taskId(taskId++)
                    .codeSubmission(codeSubmission)
                    .caseInput(problemCase.getInput())
                    .caseOutput(problemCase.getOutput())
                    .build();
            queue.offer(judgeTask);
        }
        return queue;
    }
}
