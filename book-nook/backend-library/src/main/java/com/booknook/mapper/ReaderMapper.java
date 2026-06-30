package com.booknook.mapper;

import com.booknook.entity.Reader;
import com.booknook.entity.ReaderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReaderMapper {
    long countByExample(ReaderExample example);

    int deleteByExample(ReaderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Reader row);

    int insertSelective(Reader row);

    List<Reader> selectByExample(ReaderExample example);

    Reader selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Reader row, @Param("example") ReaderExample example);

    int updateByExample(@Param("row") Reader row, @Param("example") ReaderExample example);

    int updateByPrimaryKeySelective(Reader row);

    int updateByPrimaryKey(Reader row);
}