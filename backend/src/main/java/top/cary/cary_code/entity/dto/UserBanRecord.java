package top.cary.cary_code.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserBanRecord {

    private Integer uid;

    private String banUntil;
}
