package top.cary.cary_code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.cary.cary_code.service.TutorialService;
import top.cary.cary_code.utils.Response;

@RestController
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;

    @GetMapping("/tutorial_document")
    public Response markDownDocument(@RequestParam("file_path")String filePath) {
        return tutorialService.getMarkDownDocument(filePath);
    }
}
