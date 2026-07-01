package com.booknook.service.impl;

import com.booknook.common.AuthContext;
import com.booknook.common.LoginUser;
import com.booknook.common.PageResult;
import com.booknook.exception.BusinessException;
import com.booknook.mapper.LibraryMapper;
import com.booknook.service.LibraryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 图书馆核心业务实现类。 */
@Service
public class LibraryServiceImpl implements LibraryService {
    private final LibraryMapper mapper;

    public LibraryServiceImpl(LibraryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveCategory(Long id, Map<String, Object> p) {
        String name = str(p.get("categoryName"));
        if (name.isEmpty()) throw new BusinessException("分类名称不能为空");
        if (mapper.countCategoryByName(name, id) > 0) throw new BusinessException("分类名称不能重复");
        p.put("categoryName", name);
        if (id == null) mapper.insertCategory(p); else mapper.updateCategory(id, p);
    }

    @Override
    public void deleteCategory(Long id) {
        if (mapper.deleteCategory(id) == 0) throw new BusinessException("分类正在被图书使用，不能删除");
    }

    @Override
    public void saveBook(Long id, Map<String, Object> p) {
        String isbn = str(p.get("isbn"));
        String title = str(p.get("title"));
        String author = str(p.get("author"));
        if (isbn.isEmpty()) throw new BusinessException("ISBN不能为空");
        if (title.isEmpty()) throw new BusinessException("书名不能为空");
        if (author.isEmpty()) throw new BusinessException("作者不能为空");
        if (p.get("categoryId") == null || str(p.get("categoryId")).isEmpty()) throw new BusinessException("图书分类不能为空");
        if (mapper.countBookByIsbn(isbn, id) > 0) throw new BusinessException("ISBN不能重复");
        int total = intVal(p.get("stockTotal"), 0);
        int available = intVal(p.get("stockAvailable"), total);
        if (total < 0) throw new BusinessException("馆藏总量不能小于0");
        if (available < 0 || available > total) throw new BusinessException("可借库存必须在0到馆藏总量之间");
        p.put("isbn", isbn);
        p.put("title", title);
        p.put("author", author);
        if (id == null) mapper.insertBook(p); else mapper.updateBook(id, p);
    }

    @Override
    public void deleteBook(Long id) {
        if (mapper.deleteBook(id) == 0) throw new BusinessException("该图书已有借阅记录，不能删除");
    }

    @Override
    public PageResult<Map<String, Object>> readers(Map<String, Object> p) {
        int page = page(p), size = size(p), offset = (page - 1) * size;
        String keyword = strOrNull(p.get("keyword"));
        String status = strOrNull(p.get("status"));
        return new PageResult<>(mapper.countReaders(keyword, status), mapper.listReaders(keyword, status, offset, size), page, size);
    }

    @Override
    public void saveReader(Long id, Map<String, Object> p) {
        String studentNo = str(p.get("studentNo"));
        String name = str(p.get("name"));
        String phone = str(p.get("phone"));
        if (studentNo.isEmpty()) throw new BusinessException("学号不能为空");
        if (name.isEmpty()) throw new BusinessException("读者姓名不能为空");
        if (!phone.isEmpty() && !phone.matches("^1[3-9]\\d{9}$")) throw new BusinessException("手机号格式不正确");
        if (mapper.countReaderByStudentNo(studentNo, id) > 0) throw new BusinessException("学号不能重复");
        p.put("studentNo", studentNo);
        p.put("name", name);
        p.put("phone", phone);
        if (id == null) mapper.insertReader(p); else mapper.updateReader(id, p);
    }

    @Override
    public void deleteReader(Long id) {
        if (mapper.deleteReader(id) == 0) throw new BusinessException("读者已有借阅记录，不能删除");
    }

    @Override
    @Transactional
    public void borrow(Long readerId, Long bookId) {
        if (readerId == null || bookId == null) throw new BusinessException("读者和图书不能为空");
        if (mapper.countActiveBorrow(readerId, bookId) > 0) throw new BusinessException("同一读者不能重复借阅尚未归还的同一本书");
        Map<String, Object> book = mapper.findBookForUpdate(bookId);
        if (book == null) throw new BusinessException("图书不存在");
        int before = ((Number) book.get("stock_available")).intValue();
        if (before <= 0) throw new BusinessException("库存为0，不能直接借书，请先预约");
        mapper.insertBorrow(readerId, bookId);
        if (mapper.decreaseBookStock(bookId) == 0) throw new BusinessException("库存不足，借书失败");
        mapper.increaseBorrowCount(bookId);
        mapper.insertInventoryLog(bookId, "BORROW", before, -1, before - 1, "借出图书：" + book.get("title"));
    }

    @Override
    @Transactional
    public void returnBook(Long borrowId) {
        Map<String, Object> record = mapper.findBorrowForUpdate(borrowId);
        if (record == null) throw new BusinessException("借阅记录不存在");
        String status = str(record.get("status"));
        if ("RETURNED".equals(status)) throw new BusinessException("该记录已归还");
        Long bookId = ((Number) record.get("book_id")).longValue();
        Long readerId = ((Number) record.get("reader_id")).longValue();
        Map<String, Object> book = mapper.findBookForUpdate(bookId);
        int before = ((Number) book.get("stock_available")).intValue();
        mapper.returnBorrow(borrowId);
        mapper.increaseBookStock(bookId);
        mapper.insertInventoryLog(bookId, "RETURN", before, 1, before + 1, "归还图书：" + book.get("title"));
        mapper.increaseCurrentGoal(readerId);
        Map<String, Object> first = mapper.findFirstWaitingReservation(bookId);
        if (first != null) mapper.markReservationReady(((Number) first.get("id")).longValue());
    }

    @Override
    public PageResult<Map<String, Object>> borrows(Map<String, Object> p) {
        int page = page(p), size = size(p), offset = (page - 1) * size;
        Long readerId = readerScopedId(p.get("readerId"));
        String status = strOrNull(p.get("status"));
        String keyword = strOrNull(p.get("keyword"));
        return new PageResult<>(mapper.countBorrows(readerId, status, keyword), mapper.listBorrows(readerId, status, keyword, offset, size), page, size);
    }

    @Override
    @Transactional
    public void reserve(Long readerId, Long bookId) {
        Long currentReaderId = currentReaderId();
        if (currentReaderId != null) {
            readerId = currentReaderId;
        }
        if (readerId == null || bookId == null) throw new BusinessException("读者和图书不能为空");
        if (mapper.countActiveReservation(readerId, bookId) > 0) throw new BusinessException("同一读者不能重复预约同一本有效图书");
        Map<String, Object> book = mapper.findBookForUpdate(bookId);
        if (book == null) throw new BusinessException("图书不存在");
        Integer queueNo = mapper.nextReservationQueueNo(bookId);
        mapper.insertReservation(readerId, bookId, queueNo);
    }

    @Override
    public PageResult<Map<String, Object>> reservations(Map<String, Object> p) {
        int page = page(p), size = size(p), offset = (page - 1) * size;
        Long readerId = readerScopedId(p.get("readerId"));
        String status = strOrNull(p.get("status"));
        String keyword = strOrNull(p.get("keyword"));
        return new PageResult<>(mapper.countReservations(readerId, status, keyword), mapper.listReservations(readerId, status, keyword, offset, size), page, size);
    }

    @Override
    public PageResult<Map<String, Object>> inventoryLogs(Map<String, Object> p) {
        int page = page(p), size = size(p), offset = (page - 1) * size;
        String keyword = strOrNull(p.get("keyword"));
        return new PageResult<>(mapper.countInventoryLogs(keyword), mapper.listInventoryLogs(keyword, offset, size), page, size);
    }

    @Override
    public List<Map<String, Object>> goals(Long readerId) {
        return mapper.listGoals(readerScopedId(readerId));
    }

    @Override
    public void saveGoal(Map<String, Object> p) {
        Long current = currentReaderId();
        if (current != null) {
            p.put("readerId", current);
        } else if (p.get("readerId") == null) {
            throw new BusinessException("读者不能为空");
        }
        mapper.upsertGoal(p);
    }

    @Override
    public Map<String, Object> overview() {
        Map<String, Object> data = new HashMap<>(mapper.dashboardOverview());
        data.put("hotBooks", mapper.dashboardHotBooks());
        return data;
    }

    @Override
    public List<Map<String, Object>> trend() { return mapper.dashboardTrend(); }

    @Override
    public List<Map<String, Object>> categoryDistribution() { return mapper.dashboardCategoryDistribution(); }

    @Override
    public List<Map<String, Object>> recommendations() {
        Long readerId = currentReaderId();
        Long categoryId = readerId == null ? null : mapper.favoriteCategory(readerId);
        List<Map<String, Object>> list = categoryId == null ? null : mapper.recommendByCategory(readerId, categoryId);
        return (list == null || list.isEmpty()) ? mapper.recommendHot(readerId) : list;
    }

    @Override
    public Map<String, Object> blindPick() { return mapper.blindPick(); }

    private Long readerScopedId(Object requested) {
        Long current = currentReaderId();
        if (current != null) return current;
        return longVal(requested);
    }

    private Long currentReaderId() {
        LoginUser user = AuthContext.get();
        return user != null && "READER".equals(user.getRole()) ? user.getRefId() : null;
    }

    private int page(Map<String, Object> p) { return Math.max(intVal(p.get("page"), 1), 1); }
    private int size(Map<String, Object> p) { return Math.min(Math.max(intVal(p.get("size"), 10), 1), 50); }
    private Long longVal(Object v) { try { return v == null || str(v).isEmpty() ? null : Long.parseLong(str(v)); } catch (Exception e) { return null; } }
    private int intVal(Object v, int d) { try { return v == null || str(v).isEmpty() ? d : Integer.parseInt(str(v)); } catch (Exception e) { return d; } }
    private String str(Object v) { return v == null ? "" : String.valueOf(v).trim(); }
    private String strOrNull(Object v) { String s = str(v); return s.isEmpty() ? null : s; }
}
