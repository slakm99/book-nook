package com.booknook.mapper;

import com.booknook.entity.Librarian;
import com.booknook.entity.LibrarianExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LibrarianMapper {
    long countByExample(LibrarianExample example);

    int deleteByExample(LibrarianExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Librarian row);

    int insertSelective(Librarian row);

    List<Librarian> selectByExample(LibrarianExample example);

    Librarian selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Librarian row, @Param("example") LibrarianExample example);

    int updateByExample(@Param("row") Librarian row, @Param("example") LibrarianExample example);

    int updateByPrimaryKeySelective(Librarian row);

    int updateByPrimaryKey(Librarian row);
}