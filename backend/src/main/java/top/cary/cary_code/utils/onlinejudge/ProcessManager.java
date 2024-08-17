package top.cary.cary_code.utils.onlinejudge;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ProcessManager {

    private final int DEFAULT_PROCESS_AMOUNT;

    @Value("${online-judge.lowest-process-amount}")
    private int LOWEST_PROCESS_AMOUNT;

    @Value("${online-judge.highest-process-amount}")
    private int HIGHEST_PROCESS_AMOUNT;

    int processAmount = 0;

    String host;

    int port;


    Map<Long,Process> clients = new HashMap<>();

    ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    ProcessManager(@Value("${online-judge.default-process-amount}") int DEFAULT_PROCESS_AMOUNT,
                   @Value("${online-judge.host}") String host,
                   @Value("${online-judge.port}") int port) {
        this.DEFAULT_PROCESS_AMOUNT = DEFAULT_PROCESS_AMOUNT;
        this.host = host;
        this.port = port;
        addMoreClients(DEFAULT_PROCESS_AMOUNT);
    }

    public boolean checkClientId(long clientId) {
        return clients.containsKey(clientId);
    }

    private void createClientProcess(long clientId) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("cmd");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (OutputStream outputStream = process.getOutputStream()) {
            outputStream.write("cd C:\\my\\code\\idea_project\\judger\n".getBytes());
            String launch = "mvn exec:java -Dexec.mainClass=\"top.cary61.judger.Client\" -Dexec.args=\"%s %d %d\"\n"
                    .formatted(host, port, clientId);
//            System.out.println("launch: " + launch);
            outputStream.write(launch.getBytes());
            outputStream.flush();

//            Process finalProcess = process;
//            new Thread(() -> {
//                InputStream inputStream = finalProcess.getInputStream();
//                InputStream errorStream = finalProcess.getErrorStream();
//                BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
//                BufferedReader br2 = new BufferedReader(new InputStreamReader(errorStream));
//                String line;
//                while (true) {
//                    try {
//                        if (!((line = br1.readLine()) != null)) break;
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println("line: " + line);
//                }
//                while (true) {
//                    try {
//                        if (!((line = br2.readLine()) != null)) break;
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println("lineErr: " + line);
//                }
//            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clients.put(clientId, process);
    }

    public void addMoreClients(int amount) {
        for (int i = 0; i < amount; i++) {
            long clientId = threadLocalRandom.nextLong();
            createClientProcess(clientId);
        }
    }

    // 翻倍扩容，不超过上限
    public void addMoreClients() {
        int maxCapacityLeft = HIGHEST_PROCESS_AMOUNT - clients.size();
        int addAmount = Math.min(maxCapacityLeft, clients.size());
        addMoreClients(clients.size());
    }

    public void removeClients(int amount) {
        // TO-DO
    }

    // 折半减少，不超过下限
    public void removeClients() {
        int removeAmount = Math.max(LOWEST_PROCESS_AMOUNT, clients.size() / 2);
        removeClients(removeAmount);
    }

    public void destroyAllProcesses() {
        System.out.println("销毁所有评测机进程");
        for (Process process : clients.values()) {
            process.destroyForcibly();
        }
    }
}
