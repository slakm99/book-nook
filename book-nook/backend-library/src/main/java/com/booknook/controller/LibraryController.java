package com.booknook.controller;

import com.booknook.common.AuthContext;
import com.booknook.common.LoginUser;
import com.booknook.common.PageResult;
import com.booknook.common.Result;
import com.booknook.exception.BusinessException;
import com.booknook.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 图书馆核心业务接口。 */
@RestController
public class LibraryController {
    private final LibraryService service;

    public LibraryController(LibraryService service) {
        this.service = service;
    }

    @PostMapping("/api/category")
    public Result<Void> createCategory(@RequestBody Map<String, Object> p) { requireManager(); service.saveCategory(null, p); return Result.ok(); }
    @PutMapping("/api/category/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody Map<String, Object> p) { requireManager(); service.saveCategory(id, p); return Result.ok(); }
    @DeleteMapping("/api/category/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) { requireManager(); service.deleteCategory(id); return Result.ok(); }

    @PostMapping("/api/book")
    public Result<Void> createBook(@RequestBody Map<String, Object> p) { requireManager(); service.saveBook(null, p); return Result.ok(); }
    @PutMapping("/api/book/{id}")
    public Result<Void> updateBook(@PathVariable Long id, @RequestBody Map<String, Object> p) { requireManager(); service.saveBook(id, p); return Result.ok(); }
    @DeleteMapping("/api/book/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) { requireManager(); service.deleteBook(id); return Result.ok(); }

    @GetMapping("/api/reader/list")
    public Result<PageResult<Map<String, Object>>> readers(@RequestParam Map<String, Object> p) { requireManager(); return Result.ok(service.readers(p)); }
    @PostMapping("/api/reader")
    public Result<Void> createReader(@RequestBody Map<String, Object> p) { requireManager(); service.saveReader(null, p); return Result.ok(); }
    @PutMapping("/api/reader/{id}")
    public Result<Void> updateReader(@PathVariable Long id, @RequestBody Map<String, Object> p) { requireManager(); service.saveReader(id, p); return Result.ok(); }
    @DeleteMapping("/api/reader/{id}")
    public Result<Void> deleteReader(@PathVariable Long id) { requireManager(); service.deleteReader(id); return Result.ok(); }

    @PostMapping("/api/borrow")
    public Result<Void> borrow(@RequestBody Map<String, Object> p) { requireManager(); service.borrow(num(p.get("readerId")), num(p.get("bookId"))); return Result.ok(); }
    @PutMapping("/api/borrow/{id}/return")
    public Result<Void> returnBook(@PathVariable Long id) { requireManager(); service.returnBook(id); return Result.ok(); }
    @GetMapping("/api/borrow/list")
    public Result<PageResult<Map<String, Object>>> borrows(@RequestParam Map<String, Object> p) { return Result.ok(service.borrows(p)); }

    @PostMapping("/api/reservation")
    public Result<Void> reserve(@RequestBody Map<String, Object> p) { service.reserve(num(p.get("readerId")), num(p.get("bookId"))); return Result.ok(); }
    @GetMapping("/api/reservation/list")
    public Result<PageResult<Map<String, Object>>> reservations(@RequestParam Map<String, Object> p) { return Result.ok(service.reservations(p)); }

    @GetMapping("/api/inventory-log/list")
    public Result<PageResult<Map<String, Object>>> inventory(@RequestParam Map<String, Object> p) { requireManager(); return Result.ok(service.inventoryLogs(p)); }

    @GetMapping("/api/reading-goal/list")
    public Result<List<Map<String, Object>>> goals(@RequestParam(required = false) Long readerId) { return Result.ok(service.goals(readerId)); }
    @PostMapping("/api/reading-goal")
    public Result<Void> saveGoal(@RequestBody Map<String, Object> p) { service.saveGoal(p); return Result.ok(); }

    @GetMapping("/api/dashboard/overview")
    public Result<Map<String, Object>> overview() { return Result.ok(service.overview()); }
    @GetMapping("/api/dashboard/trend")
    public Result<List<Map<String, Object>>> trend() { return Result.ok(service.trend()); }
    @GetMapping("/api/dashboard/category-distribution")
    public Result<List<Map<String, Object>>> categoryDistribution() { return Result.ok(service.categoryDistribution()); }
    @GetMapping("/api/recommendation/me")
    public Result<List<Map<String, Object>>> recommendation() { return Result.ok(service.recommendations()); }
    @GetMapping("/api/book/blind-pick")
    public Result<Map<String, Object>> blindPick() { return Result.ok(service.blindPick()); }

    private Long num(Object value) {
        if (value == null) return null;
        return Long.parseLong(String.valueOf(value));
    }

    private void requireManager() {
        LoginUser user = AuthContext.get();
        if (user == null || (!"ADMIN".equals(user.getRole()) && !"LIBRARIAN".equals(user.getRole()))) {
            throw new BusinessException(403, "读者无权进行后台管理操作");
        }
    }
}
