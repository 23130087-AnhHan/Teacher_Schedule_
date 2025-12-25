package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng constraint_violations
 * Ghi nhận các vi phạm ràng buộc
 */
public class ConstraintViolation {
    private int violationId;
    private int logId;
    private Integer scheduleId;  // Nullable
    private ViolationType violationType;
    private Severity severity;
    private String description;
    private LocalDateTime createdAt;

    // Enums
    public enum ViolationType {
        TEACHER_TIME_CONFLICT,      // Giáo viên dạy 2 lớp cùng lúc
        ROOM_TIME_CONFLICT,          // Phòng bị trùng
        TEACHER_SUBJECT_MISMATCH,    // GV dạy môn không phụ trách
        ROOM_CAPACITY_EXCEEDED,      // Phòng quá nhỏ
        ROOM_TYPE_MISMATCH,          // TH không dùng Lab hoặc LT dùng Lab
        MAX_HOURS_EXCEEDED,          // Vượt giờ dạy tối đa
        UNSCHEDULED_CLASS            // Lớp chưa được xếp
    }

    public enum Severity {
        CRITICAL,  // Vi phạm nghiêm trọng (hard constraint)
        HIGH,
        MEDIUM,
        LOW        // Vi phạm nhẹ (soft constraint)
    }

    // Constructors
    public ConstraintViolation() {
    }

    public ConstraintViolation(int logId, Integer scheduleId, ViolationType violationType, 
                               Severity severity, String description) {
        this.logId = logId;
        this.scheduleId = scheduleId;
        this.violationType = violationType;
        this.severity = severity;
        this.description = description;
    }

    // Getters and Setters
    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isCritical() {
        return severity == Severity.CRITICAL;
    }

    public boolean isHardConstraint() {
        return severity == Severity.CRITICAL || severity == Severity.HIGH;
    }

    public String getViolationMessage() {
        switch (violationType) {
            case TEACHER_TIME_CONFLICT:
                return "Giáo viên dạy 2 lớp cùng lúc";
            case ROOM_TIME_CONFLICT:
                return "Phòng học bị trùng lịch";
            case TEACHER_SUBJECT_MISMATCH: 
                return "Giáo viên dạy môn không phụ trách";
            case ROOM_CAPACITY_EXCEEDED:
                return "Phòng không đủ chỗ";
            case ROOM_TYPE_MISMATCH:
                return "Loại phòng không phù hợp";
            case MAX_HOURS_EXCEEDED:
                return "Vượt giờ dạy tối đa";
            case UNSCHEDULED_CLASS:
                return "Lớp chưa được xếp lịch";
            default: 
                return "Vi phạm không xác định";
        }
    }

    @Override
    public String toString() {
        return "ConstraintViolation{" +
                "violationId=" + violationId +
                ", violationType=" + violationType +
                ", severity=" + severity +
                ", description='" + description + '\'' +
                '}';
    }
}