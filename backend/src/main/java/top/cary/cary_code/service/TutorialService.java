package top.cary.cary_code.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import top.cary.cary_code.utils.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class TutorialService {

    public Response getMarkDownDocument(String filePath) {
        ClassPathResource classPathResource = new ClassPathResource("static/docs/" + filePath);
        try (InputStream inputStream = classPathResource.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[8192];
            while (bufferedReader.read(buffer) > 0) {
                sb.append(buffer);
            }
            return Response.ok(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
