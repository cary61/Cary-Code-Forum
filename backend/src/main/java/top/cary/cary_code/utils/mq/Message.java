package top.cary.cary_code.utils.mq;

import lombok.Builder;
import lombok.Data;

import java.io.*;
import java.time.LocalDateTime;

@Data
@Builder
public class Message <T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String topic;

    private T data;

    private Long id;

    private LocalDateTime time;

}
