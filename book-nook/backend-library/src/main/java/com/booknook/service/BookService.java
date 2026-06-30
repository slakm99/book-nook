package com.booknook.service;

import com.booknook.common.PageResult;

import java.util.List;
import java.util.Map;

/** 图书查询业务接口。 */
public interface BookService {
    PageResult<Map<String, Object>> listBooks(Map<String, Object> params);

    List<Map<String, Object>> listCategories();
}
