package com.booknook.controller;

import com.booknook.common.PageResult;
import com.booknook.common.Result;
import com.booknook.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/** 图书查询接口。 */
@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/book/list")
    public Result<PageResult<Map<String, Object>>> list(@RequestParam Map<String, Object> params) {
        return Result.ok(bookService.listBooks(params));
    }

    @GetMapping("/api/category/list")
    public Result<List<Map<String, Object>>> categories() {
        return Result.ok(bookService.listCategories());
    }
}
