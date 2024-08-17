package top.cary.cary_code.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Article implements Text {

    private Integer id;

    private Integer uid;

    private String nickname;

    private String name;

    private String avatar;

    // 是否被用户点赞过
    private Boolean liked;

    // 是否被用户点踩过
    private Boolean hated;

    // 是否被用户收藏过
    private Boolean marked;

    private String title;

    private String content;

    private String type;

    private Integer likes;

    private Integer hates;

    private Integer commentsAmount;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime latestOperateTime;
}
