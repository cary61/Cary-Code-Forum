package top.cary.cary_code.utils.onlinejudge;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary.cary_code.entity.CodeSubmission;
import top.cary.cary_code.entity.JudgeTask;
import top.cary.cary_code.entity.JudgeTaskResult;
import top.cary.cary_code.entity.unum.LanguageEnum;
import top.cary.cary_code.service.JudgeService;
import top.cary.cary_code.utils.mq.Message;
import top.cary.cary_code.utils.mq.MessageQueue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class JudgeServer implements Runnable {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JudgeService judgeService;


    @Value("${online-judge.host}")
    private String host;
    private final int port;
    private final ServerSocket serverSocket;
    private final ProcessManager processManager;
    private final JudgeTaskManager judgeTaskManager;

    MessageQueue messageQueue;

    Thread thread;

    JudgeServer(@Value("${online-judge.port}") int port,
                @Autowired ProcessManager processManager,
                @Autowired JudgeTaskManager judgeTaskManager,
                @Autowired MessageQueue messageQueue)
            throws IOException {
        this.port = port;
        this.processManager = processManager;
        this.judgeTaskManager = judgeTaskManager;
        serverSocket = new ServerSocket(port);
//        this.messageQueue = messageQueue;
//        addTestCases();
        thread = new Thread(this, "JudgeServer");
        thread.start();
    }

    public void run() {
        System.out.println("评测主服务器已启动，监听端口：" + port);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("连接到客户端");
                answer(socket);
                System.out.println("客户端已退出");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void answer(Socket client) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            String head = br.readLine();
            System.out.println(head);
            long clientId = Long.parseLong(br.readLine());
            if (!processManager.checkClientId(clientId)) {
                return;
            }
            System.out.println("客户端id：" + clientId);
            if (Objects.equals(head, "ASK")) {
                answerAsk(br, bw);
            } else if (Objects.equals(head, "RESULT")) {
                answerResult(br, bw);
            } else {
                throw new RuntimeException("INVALID REQUEST");
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void answerAsk(BufferedReader br, BufferedWriter bw) throws IOException {
        JudgeTask judgeTask = judgeTaskManager.requestJudgeTask();
        if (judgeTask == null) {
            bw.write("NOT AVAILABLE\n");  bw.flush();
            return;
        }
        bw.write("AVAILABLE\n");  bw.flush();
        String judgeTaskJson = objectMapper.writeValueAsString(judgeTask);
        bw.write(judgeTaskJson + "\n");  bw.flush();
    }

    private void answerResult(BufferedReader br, BufferedWriter bw) throws IOException {
        String judgeTaskResultJson = br.readLine();
        JudgeTaskResult judgeTaskResult = objectMapper.readValue(judgeTaskResultJson, JudgeTaskResult.class);
        judgeTaskManager.uploadResult(judgeTaskResult);
        processManager.addMoreClients(1);
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        serverSocket.close();
        processManager.destroyAllProcesses();
    }

    private void addTestCases() {
        // test code
        CodeSubmission codeSubmission = new CodeSubmission();
        codeSubmission.setCode("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int a = sc.nextInt(), b = sc.nextInt();
                            System.out.println(a - b);
                        }
                    }
                """);
        codeSubmission.setUid(2);
        codeSubmission.setProblemId(1);
        codeSubmission.setLanguage(LanguageEnum.JAVA);
        messageQueue.offer(Message.builder()
                .id(1L)
                .topic("JUDGE")
                .time(LocalDateTime.now())
                .data(codeSubmission)
                .build());
        CodeSubmission codeSubmission2 = new CodeSubmission();
        codeSubmission2.setCode("""
                    import java.util.*;
                    public class Main {
                        public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                            int a = sc.nextInt(), b = sc.nextInt();
                            System.out.println(a + b);
                        }
                    }
                """);
        codeSubmission2.setUid(2);
        codeSubmission2.setProblemId(1);
        codeSubmission2.setLanguage(LanguageEnum.JAVA);
        messageQueue.offer(Message.builder()
                .id(2L)
                .topic("JUDGE")
                .time(LocalDateTime.now())
                .data(codeSubmission2)
                .build());
    }


}
