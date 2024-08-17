package top.cary.cary_code.entity;

public interface Text {

    Integer getId();

    Integer getUid();
    void setUid(Integer uid);

    String getContent();
    void setContent(String content);

    Integer getLikes();
    void setLikes(Integer likes);

    Integer getHates();
    void setHates(Integer hates);

}
