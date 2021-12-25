package org.example.Mapper;

import org.example.AllClass.Mark;

import java.util.List;

/**
 * Mark类对应的MarkMapper，其中有和数据库结合的方法
 */
public interface MarkMapper {

     Integer InsertMark(Mark mark);

     List<Mark> getAllByVideoId(Integer MyVideoId);

}
