package com.booknook.service.impl;

import com.booknook.common.PageResult;
import com.booknook.mapper.LibraryMapper;
import com.booknook.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/** 图书查询业务实现类。 */
@Service
public class BookServiceImpl implements BookService {
    private final LibraryMapper mapper;

    public BookServiceImpl(LibraryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PageResult<Map<String, Object>> listBooks(Map<String, Object> params) {
        int page = parseInt(params.get("page"), 1);
        int size = parseInt(params.get("size"), 10);
        page = Math.max(page, 1);
        size = Math.min(Math.max(size, 1), 50);
        int offset = (page - 1) * size;
        String keyword = text(params.get("keyword"));
        String author = text(params.get("author"));
        String stockStatus = text(params.get("stockStatus"));
        Long categoryId = parseLong(params.get("categoryId"));
        List<Map<String, Object>> list = mapper.listBooks(keyword, author, categoryId, stockStatus, offset, size);
        long total = mapper.countBooks(keyword, author, categoryId, stockStatus);
        return new PageResult<>(total, list, page, size);
    }

    @Override
    public List<Map<String, Object>> listCategories() {
        return mapper.listCategories();
    }

    private String text(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private int parseInt(Object value, int defaultValue) {
        try {
            return value == null || String.valueOf(value).isEmpty() ? defaultValue : Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Long parseLong(Object value) {
        try {
            return value == null || String.valueOf(value).isEmpty() ? null : Long.parseLong(String.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }
}
