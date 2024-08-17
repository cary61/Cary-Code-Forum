package top.cary.cary_code.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import top.cary.cary_code.entity.dto.UserDTO;
import top.cary.cary_code.entity.unum.AuthorityEnum;

import java.time.LocalDateTime;

@Data
@Builder
public class User {

    private Integer uid;

    private String name;

    private String nickname;

    private String passwordHash;

    private AuthorityEnum authority;

    private String avatar;

    private String signature;

    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime registerTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime bannedUntil;

    public UserDTO toDTO() {
        return UserDTO.builder()
                .uid(uid)
                .name(name)
                .nickname(nickname)
                .authority(authority)
                .avatar(avatar)
                .signature(signature)
                .registerTime(registerTime)
                .bannedUntil(bannedUntil)
                .build();
    }
}
