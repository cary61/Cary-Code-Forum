package top.cary.cary_code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.cary.cary_code.service.FileService;
import top.cary.cary_code.utils.Response;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("")
    public Response upload(@RequestBody MultipartFile multipartFile) {
        return fileService.upload(multipartFile);
    }
}
