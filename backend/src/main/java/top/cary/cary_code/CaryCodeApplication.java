package top.cary.cary_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CaryCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaryCodeApplication.class, args);
    }
}
