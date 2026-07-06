package com.booknook.service;

import com.booknook.common.PageResult;

import java.util.List;
import java.util.Map;

/** 图书馆核心业务接口。 */
public interface LibraryService {
    void saveCategory(Long id, Map<String, Object> p);
    void deleteCategory(Long id);
    void saveBook(Long id, Map<String, Object> p);
    void deleteBook(Long id);
    PageResult<Map<String, Object>> readers(Map<String, Object> p);
    void saveReader(Long id, Map<String, Object> p);
    void deleteReader(Long id);
    void borrow(Long readerId, Long bookId);
    void returnBook(Long borrowId);
    PageResult<Map<String, Object>> borrows(Map<String, Object> p);
    void reserve(Long readerId, Long bookId);
    void cancelReservation(Long reservationId);
    void pickupReservation(Long reservationId);
    PageResult<Map<String, Object>> reservations(Map<String, Object> p);
    PageResult<Map<String, Object>> inventoryLogs(Map<String, Object> p);
    List<Map<String, Object>> goals(Long readerId);
    void saveGoal(Map<String, Object> p);
    Map<String, Object> overview();
    List<Map<String, Object>> trend();
    List<Map<String, Object>> categoryDistribution();
    Map<String, Object> readerStats();
    List<Map<String, Object>> recommendations();
    Map<String, Object> blindPick();
}
