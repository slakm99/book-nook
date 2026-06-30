package com.booknook.mapper;

import com.booknook.entity.InventoryLog;
import com.booknook.entity.InventoryLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InventoryLogMapper {
    long countByExample(InventoryLogExample example);

    int deleteByExample(InventoryLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(InventoryLog row);

    int insertSelective(InventoryLog row);

    List<InventoryLog> selectByExample(InventoryLogExample example);

    InventoryLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") InventoryLog row, @Param("example") InventoryLogExample example);

    int updateByExample(@Param("row") InventoryLog row, @Param("example") InventoryLogExample example);

    int updateByPrimaryKeySelective(InventoryLog row);

    int updateByPrimaryKey(InventoryLog row);
}