package com.booknook.mapper;

import com.booknook.entity.BookCategory;
import com.booknook.entity.BookCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookCategoryMapper {
    long countByExample(BookCategoryExample example);

    int deleteByExample(BookCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BookCategory row);

    int insertSelective(BookCategory row);

    List<BookCategory> selectByExample(BookCategoryExample example);

    BookCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") BookCategory row, @Param("example") BookCategoryExample example);

    int updateByExample(@Param("row") BookCategory row, @Param("example") BookCategoryExample example);

    int updateByPrimaryKeySelective(BookCategory row);

    int updateByPrimaryKey(BookCategory row);
}