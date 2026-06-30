package com.booknook.mapper;

import com.booknook.entity.ReadingGoal;
import com.booknook.entity.ReadingGoalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReadingGoalMapper {
    long countByExample(ReadingGoalExample example);

    int deleteByExample(ReadingGoalExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ReadingGoal row);

    int insertSelective(ReadingGoal row);

    List<ReadingGoal> selectByExample(ReadingGoalExample example);

    ReadingGoal selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") ReadingGoal row, @Param("example") ReadingGoalExample example);

    int updateByExample(@Param("row") ReadingGoal row, @Param("example") ReadingGoalExample example);

    int updateByPrimaryKeySelective(ReadingGoal row);

    int updateByPrimaryKey(ReadingGoal row);
}