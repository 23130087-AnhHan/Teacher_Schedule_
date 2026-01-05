package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng teacher_preferences
 * Lưu sở thích/hạn chế của giáo viên (dùng cho soft constraints)
 */
public class TeacherPreference {
    private int preferenceId;
    private int teacherId;
    private Integer unavailableSlotId;  // Nullable - slot không thể dạy
    private TimeSlot. SessionType preferredSession;  // Nullable
    private TimeSlot.DayOfWeek preferredDay;  // Nullable
    private double weight;// trọng số ưu tiên
    private String notes;
    private LocalDateTime createdAt;

    // Constructors
    public TeacherPreference() {
    }

    public TeacherPreference(int teacherId, Integer unavailableSlotId, 
                             TimeSlot.SessionType preferredSession, 
                             TimeSlot.DayOfWeek preferredDay, double weight, String notes) {
        this.teacherId = teacherId;
        this.unavailableSlotId = unavailableSlotId;
        this.preferredSession = preferredSession;
        this.preferredDay = preferredDay;
        this.weight = weight;
        this.notes = notes;
    }

    // Getters and Setters
    public int getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getUnavailableSlotId() {
        return unavailableSlotId;
    }

    public void setUnavailableSlotId(Integer unavailableSlotId) {
        this.unavailableSlotId = unavailableSlotId;
    }

    public TimeSlot.SessionType getPreferredSession() {
        return preferredSession;
    }

    public void setPreferredSession(TimeSlot.SessionType preferredSession) {
        this.preferredSession = preferredSession;
    }

    public TimeSlot.DayOfWeek getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(TimeSlot.DayOfWeek preferredDay) {
        this.preferredDay = preferredDay;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean hasUnavailableSlot() {
        return unavailableSlotId != null;
    }

    public boolean hasPreferredSession() {
        return preferredSession != null;
    }

    public boolean hasPreferredDay() {
        return preferredDay != null;
    }

    @Override
    public String toString() {
        return "TeacherPreference{" +
                "preferenceId=" + preferenceId +
                ", teacherId=" + teacherId +
                ", unavailableSlotId=" + unavailableSlotId +
                ", preferredSession=" + preferredSession +
                ", preferredDay=" + preferredDay +
                ", weight=" + weight +
                '}';
    }
}