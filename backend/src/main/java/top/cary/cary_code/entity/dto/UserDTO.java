package top.cary.cary_code.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import top.cary.cary_code.entity.unum.AuthorityEnum;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {

    private Integer uid;

    private String name;

    private String nickname;

    private AuthorityEnum authority;

    private String avatar;

    private String signature;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime registerTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime bannedUntil;
}