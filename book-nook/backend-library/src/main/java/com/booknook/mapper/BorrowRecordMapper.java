package com.booknook.mapper;

import com.booknook.entity.BorrowRecord;
import com.booknook.entity.BorrowRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowRecordMapper {
    long countByExample(BorrowRecordExample example);

    int deleteByExample(BorrowRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BorrowRecord row);

    int insertSelective(BorrowRecord row);

    List<BorrowRecord> selectByExample(BorrowRecordExample example);

    BorrowRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") BorrowRecord row, @Param("example") BorrowRecordExample example);

    int updateByExample(@Param("row") BorrowRecord row, @Param("example") BorrowRecordExample example);

    int updateByPrimaryKeySelective(BorrowRecord row);

    int updateByPrimaryKey(BorrowRecord row);
}