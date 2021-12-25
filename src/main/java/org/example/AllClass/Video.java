package org.example.AllClass;

public class Video {
    private Integer Video_id;
    private Integer Video_order;
    private String Video_name;

    public Integer getVideo_id() {
        return Video_id;
    }

    public void setVideo_id(Integer video_id) {
        Video_id = video_id;
    }

    public Integer getVideo_order() {
        return Video_order;
    }

    public void setVideo_order(Integer video_order) {
        Video_order = video_order;
    }

    public String getVideo_name() {
        return Video_name;
    }

    public void setVideo_name(String video_name) {
        Video_name = video_name;
    }


    @Override
    public String toString() {
        return "{视频信息：\n" +
                "视频序号-" + Video_order +
                "\n视频名称:" + Video_name + '}' ;
    }
}
