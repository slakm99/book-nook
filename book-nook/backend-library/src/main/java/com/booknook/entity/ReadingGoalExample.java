package com.booknook.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadingGoalExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReadingGoalExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andReaderIdIsNull() {
            addCriterion("reader_id is null");
            return (Criteria) this;
        }

        public Criteria andReaderIdIsNotNull() {
            addCriterion("reader_id is not null");
            return (Criteria) this;
        }

        public Criteria andReaderIdEqualTo(Long value) {
            addCriterion("reader_id =", value, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdNotEqualTo(Long value) {
            addCriterion("reader_id <>", value, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdGreaterThan(Long value) {
            addCriterion("reader_id >", value, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("reader_id >=", value, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdLessThan(Long value) {
            addCriterion("reader_id <", value, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdLessThanOrEqualTo(Long value) {
            addCriterion("reader_id <=", value, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdIn(List<Long> values) {
            addCriterion("reader_id in", values, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdNotIn(List<Long> values) {
            addCriterion("reader_id not in", values, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdBetween(Long value1, Long value2) {
            addCriterion("reader_id between", value1, value2, "readerId");
            return (Criteria) this;
        }

        public Criteria andReaderIdNotBetween(Long value1, Long value2) {
            addCriterion("reader_id not between", value1, value2, "readerId");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodIsNull() {
            addCriterion("target_period is null");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodIsNotNull() {
            addCriterion("target_period is not null");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodEqualTo(String value) {
            addCriterion("target_period =", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodNotEqualTo(String value) {
            addCriterion("target_period <>", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodGreaterThan(String value) {
            addCriterion("target_period >", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodGreaterThanOrEqualTo(String value) {
            addCriterion("target_period >=", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodLessThan(String value) {
            addCriterion("target_period <", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodLessThanOrEqualTo(String value) {
            addCriterion("target_period <=", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodLike(String value) {
            addCriterion("target_period like", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodNotLike(String value) {
            addCriterion("target_period not like", value, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodIn(List<String> values) {
            addCriterion("target_period in", values, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodNotIn(List<String> values) {
            addCriterion("target_period not in", values, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodBetween(String value1, String value2) {
            addCriterion("target_period between", value1, value2, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetPeriodNotBetween(String value1, String value2) {
            addCriterion("target_period not between", value1, value2, "targetPeriod");
            return (Criteria) this;
        }

        public Criteria andTargetCountIsNull() {
            addCriterion("target_count is null");
            return (Criteria) this;
        }

        public Criteria andTargetCountIsNotNull() {
            addCriterion("target_count is not null");
            return (Criteria) this;
        }

        public Criteria andTargetCountEqualTo(Integer value) {
            addCriterion("target_count =", value, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountNotEqualTo(Integer value) {
            addCriterion("target_count <>", value, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountGreaterThan(Integer value) {
            addCriterion("target_count >", value, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("target_count >=", value, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountLessThan(Integer value) {
            addCriterion("target_count <", value, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountLessThanOrEqualTo(Integer value) {
            addCriterion("target_count <=", value, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountIn(List<Integer> values) {
            addCriterion("target_count in", values, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountNotIn(List<Integer> values) {
            addCriterion("target_count not in", values, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountBetween(Integer value1, Integer value2) {
            addCriterion("target_count between", value1, value2, "targetCount");
            return (Criteria) this;
        }

        public Criteria andTargetCountNotBetween(Integer value1, Integer value2) {
            addCriterion("target_count not between", value1, value2, "targetCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountIsNull() {
            addCriterion("completed_count is null");
            return (Criteria) this;
        }

        public Criteria andCompletedCountIsNotNull() {
            addCriterion("completed_count is not null");
            return (Criteria) this;
        }

        public Criteria andCompletedCountEqualTo(Integer value) {
            addCriterion("completed_count =", value, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountNotEqualTo(Integer value) {
            addCriterion("completed_count <>", value, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountGreaterThan(Integer value) {
            addCriterion("completed_count >", value, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("completed_count >=", value, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountLessThan(Integer value) {
            addCriterion("completed_count <", value, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountLessThanOrEqualTo(Integer value) {
            addCriterion("completed_count <=", value, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountIn(List<Integer> values) {
            addCriterion("completed_count in", values, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountNotIn(List<Integer> values) {
            addCriterion("completed_count not in", values, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountBetween(Integer value1, Integer value2) {
            addCriterion("completed_count between", value1, value2, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCompletedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("completed_count not between", value1, value2, "completedCount");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}