package com.booknook.entity;

import java.util.Date;

public class BorrowReservation {
    private Long id;

    private Long readerId;

    private Long bookId;

    private Integer queueNo;

    private String status;

    private Date pickupDeadline;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(Integer queueNo) {
        this.queueNo = queueNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getPickupDeadline() {
        return pickupDeadline;
    }

    public void setPickupDeadline(Date pickupDeadline) {
        this.pickupDeadline = pickupDeadline;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}