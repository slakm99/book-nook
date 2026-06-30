package com.booknook.mapper;

import com.booknook.entity.BorrowReservation;
import com.booknook.entity.BorrowReservationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowReservationMapper {
    long countByExample(BorrowReservationExample example);

    int deleteByExample(BorrowReservationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BorrowReservation row);

    int insertSelective(BorrowReservation row);

    List<BorrowReservation> selectByExample(BorrowReservationExample example);

    BorrowReservation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") BorrowReservation row, @Param("example") BorrowReservationExample example);

    int updateByExample(@Param("row") BorrowReservation row, @Param("example") BorrowReservationExample example);

    int updateByPrimaryKeySelective(BorrowReservation row);

    int updateByPrimaryKey(BorrowReservation row);
}