package org.example.Mapper;

import org.example.AllClass.Mark;
import org.example.AllClass.Video;

import javax.swing.*;
import java.util.List;

/**
 * 已经有sql语句，已经有Video类，mybatis会自动帮助生成一个类，调用方法
 * 此方法对应一个Video配置文件里面的一条sql
 */
public interface VideoMapper {

    //Video video=new Video();
    //接口的fineAll方法，对应Mapper里面的一个查询语句，且id就叫findAll

    Video findAllByName(String video_name);

    String findVideoNameByOrder(Integer video_order);

    Integer findVideoIdByName(String VideoName);

    List<Video> findAll();

    Integer findAllCount();

    Integer InsertVideo(Video video);


}
