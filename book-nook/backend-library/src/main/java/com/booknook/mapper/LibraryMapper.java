package com.booknook.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 图书馆业务查询 Mapper，MBG 生成的 9 表 Mapper 与它并存。 */
public interface LibraryMapper {
    Map<String, Object> findUserByUsername(@Param("username") String username);

    List<Map<String, Object>> listCategories();

    List<Map<String, Object>> listBooks(@Param("keyword") String keyword,
                                        @Param("author") String author,
                                        @Param("categoryId") Long categoryId,
                                        @Param("stockStatus") String stockStatus,
                                        @Param("offset") int offset,
                                        @Param("size") int size);

    long countBooks(@Param("keyword") String keyword,
                    @Param("author") String author,
                    @Param("categoryId") Long categoryId,
                    @Param("stockStatus") String stockStatus);

    int countCategoryByName(@Param("name") String name, @Param("id") Long id);
    int insertCategory(@Param("p") Map<String, Object> p);
    int updateCategory(@Param("id") Long id, @Param("p") Map<String, Object> p);
    int deleteCategory(@Param("id") Long id);
    int countBookByIsbn(@Param("isbn") String isbn, @Param("id") Long id);
    Map<String, Object> findBookForUpdate(@Param("id") Long id);
    int insertBook(@Param("p") Map<String, Object> p);
    int updateBook(@Param("id") Long id, @Param("p") Map<String, Object> p);
    int deleteBook(@Param("id") Long id);

    List<Map<String, Object>> listReaders(@Param("keyword") String keyword, @Param("status") String status, @Param("offset") int offset, @Param("size") int size);
    long countReaders(@Param("keyword") String keyword, @Param("status") String status);
    int countReaderByStudentNo(@Param("studentNo") String studentNo, @Param("id") Long id);
    int insertReader(@Param("p") Map<String, Object> p);
    int updateReader(@Param("id") Long id, @Param("p") Map<String, Object> p);
    int deleteReader(@Param("id") Long id);

    int countActiveBorrow(@Param("readerId") Long readerId, @Param("bookId") Long bookId);
    int insertBorrow(@Param("readerId") Long readerId, @Param("bookId") Long bookId);
    int decreaseBookStock(@Param("bookId") Long bookId);
    int increaseBookStock(@Param("bookId") Long bookId);
    int increaseBorrowCount(@Param("bookId") Long bookId);
    Map<String, Object> findBorrowForUpdate(@Param("id") Long id);
    int returnBorrow(@Param("id") Long id);
    List<Map<String, Object>> listBorrows(@Param("readerId") Long readerId, @Param("status") String status, @Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
    long countBorrows(@Param("readerId") Long readerId, @Param("status") String status, @Param("keyword") String keyword);

    int insertInventoryLog(@Param("bookId") Long bookId, @Param("changeType") String changeType, @Param("beforeStock") Integer beforeStock, @Param("changeQuantity") Integer changeQuantity, @Param("afterStock") Integer afterStock, @Param("remark") String remark);
    List<Map<String, Object>> listInventoryLogs(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
    long countInventoryLogs(@Param("keyword") String keyword);

    int countActiveReservation(@Param("readerId") Long readerId, @Param("bookId") Long bookId);
    Integer nextReservationQueueNo(@Param("bookId") Long bookId);
    int insertReservation(@Param("readerId") Long readerId, @Param("bookId") Long bookId, @Param("queueNo") Integer queueNo, @Param("status") String status);
    Map<String, Object> findReservationForUpdate(@Param("id") Long id);
    Map<String, Object> findFirstWaitingReservation(@Param("bookId") Long bookId);
    int markReservationReady(@Param("id") Long id);
    int finishReservation(@Param("id") Long id);
    int cancelReservation(@Param("id") Long id, @Param("readerId") Long readerId);
    int expireReadyReservations();
    List<Map<String, Object>> listReservations(@Param("readerId") Long readerId, @Param("status") String status, @Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
    long countReservations(@Param("readerId") Long readerId, @Param("status") String status, @Param("keyword") String keyword);

    List<Map<String, Object>> listGoals(@Param("readerId") Long readerId);
    int upsertGoal(@Param("p") Map<String, Object> p);
    int increaseCurrentGoal(@Param("readerId") Long readerId);

    Map<String, Object> dashboardOverview();
    List<Map<String, Object>> dashboardTrend();
    List<Map<String, Object>> dashboardCategoryDistribution();
    List<Map<String, Object>> dashboardHotBooks();
    Long favoriteCategory(@Param("readerId") Long readerId);
    List<Map<String, Object>> recommendByCategory(@Param("readerId") Long readerId, @Param("categoryId") Long categoryId);
    List<Map<String, Object>> recommendHot(@Param("readerId") Long readerId);
    Map<String, Object> blindPick();
}
