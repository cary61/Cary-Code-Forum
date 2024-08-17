package top.cary.cary_code.controller;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.cary.cary_code.entity.CaseDTO;
import top.cary.cary_code.entity.CodeSubmission;
import top.cary.cary_code.entity.Problem;
import top.cary.cary_code.service.JudgeService;
import top.cary.cary_code.utils.Response;



@RestController
@RequestMapping("/judge")
public class JudgeController {

    @Autowired
    JudgeService judgeService;

    @PostMapping("/source_code")
    public Response sourceCode(@RequestBody CodeSubmission codeSubmission) {
        return judgeService.judge(codeSubmission);
    }

    @GetMapping("/problem_list")
    public Response problemList() {
        return judgeService.getProblemList();
    }

    @GetMapping("/problem")
    public Response problem(@RequestParam(value = "problem_id", required = false) Integer problemId,
                            @RequestParam(value = "problem_name", required = false) String name) {
        if (problemId != null) {
            return judgeService.getProblem(problemId);
        }
        if (name != null) {
            System.out.println(name);
            return judgeService.getProblemByName(name);
        }
        return Response.failWithMsg("参数无效");
    }

    @DeleteMapping("/problem")
    public Response deleteProblem(@RequestParam("problem_id") Integer problemId) {
        return judgeService.deleteProblem(problemId);
    }

    @GetMapping("/result")
    public Response result(@RequestParam("polling_id") String pollingId) {
        System.out.println("pollingId: " + pollingId);
        return judgeService.getResult(Long.parseLong(pollingId));
    }

    @PostMapping("/problem")
    public Response addProblem(@RequestBody Problem problem) {
        if (problem.getName() == null || problem.getName().isBlank()) {
            return Response.failWithMsg("缺少标题");
        }
        return judgeService.addProblem(problem);
    }

    @PostMapping("/case")
    public Response addCase(@RequestBody CaseDTO caseDTO) {
        try {
            return judgeService.addCase(caseDTO);
        } catch (Exception e)  {
            e.printStackTrace();
        return Response.failWithMsg("exception occurs");
        }
    }

}
