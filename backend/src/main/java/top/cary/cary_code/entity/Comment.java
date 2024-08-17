package top.cary.cary_code.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment implements Text {

    private Integer id;

    private Integer articleId;

    private Integer commentId;

    private Integer uid;

    private String nickname;

    private String avatar;

    // 是否被用户点赞过
    private Boolean liked;

    // 是否被用户点踩过
    private Boolean hated;

    private String content;

    private Integer likes;

    private Integer hates;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
}
