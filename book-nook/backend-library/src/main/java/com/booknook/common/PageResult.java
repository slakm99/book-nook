package com.booknook.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** 分页返回对象。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private long total;
    private List<T> list;
    private int page;
    private int size;
}
