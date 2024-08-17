package top.cary.cary_code.entity;

import lombok.Builder;
import lombok.Data;
import top.cary.cary_code.entity.unum.PollingStatusEnum;

@Data
@Builder
public class PollingMessage <T> {

    private String pollingId;

    private PollingStatusEnum pollingStatus;

    private Long timeIntervalMillis;

    private T data;
}
