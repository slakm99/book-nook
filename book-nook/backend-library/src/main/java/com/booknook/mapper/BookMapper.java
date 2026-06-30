package com.booknook.mapper;

import com.booknook.entity.Book;
import com.booknook.entity.BookExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookMapper {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Book row);

    int insertSelective(Book row);

    List<Book> selectByExampleWithBLOBs(BookExample example);

    List<Book> selectByExample(BookExample example);

    Book selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Book row, @Param("example") BookExample example);

    int updateByExampleWithBLOBs(@Param("row") Book row, @Param("example") BookExample example);

    int updateByExample(@Param("row") Book row, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(Book row);

    int updateByPrimaryKeyWithBLOBs(Book row);

    int updateByPrimaryKey(Book row);
}