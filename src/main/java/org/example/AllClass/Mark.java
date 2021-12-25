package org.example.AllClass;

public class Mark {

    private Integer Mark_id;
    private String Mark_text;
    private Integer MyVideoId;
    private Long Mark_time;

    public Integer getMark_id() {
        return Mark_id;
    }
    public void setMark_id(Integer mark_id) {
        Mark_id = mark_id;
    }

    public Long getMark_time() {
        return Mark_time;
    }

    public void setMark_time(Long mark_time) {
        Mark_time = mark_time;
    }

    public String getMark_text() {
        return Mark_text;
    }

    public void setMark_text(String mark_text) {
        Mark_text = mark_text;
    }

    public Integer getMyVideoId() {
        return MyVideoId;
    }

    public void setMyVideoId(Integer myVideoId) {
        MyVideoId = myVideoId;
    }

    @Override
    public String toString() {
        return "书签：\n" +
                "\n书签内容：\n'" + Mark_text + '\'' +
                '}';
    }
}
