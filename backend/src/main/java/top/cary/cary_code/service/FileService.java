package top.cary.cary_code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.cary.cary_code.utils.encryption.Hasher;
import top.cary.cary_code.utils.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class FileService {

    @Value("${web-config.file-data-path}")
    private String fileDataPath;

    @Value("${web-config.file-path-prefix}")
    private String filePathPrefix;

    @Autowired
    Hasher hasher;

    public Response upload(MultipartFile multipartFile) {
        String fileName = hasher.hashFileName() + "_" + multipartFile.getOriginalFilename();
        String filePath = fileDataPath + filePathPrefix + fileName;
        try {
            File file = new File(filePath);
            file.createNewFile();
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Response.ok(Map.of("file_path", "/" + filePathPrefix + fileName));
    }
}
