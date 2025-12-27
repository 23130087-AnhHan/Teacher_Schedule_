package model;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Model class cho bảng time_slots
 */
public class TimeSlot {
    private int slotId;
    private DayOfWeek dayOfWeek;
    private int blockNumber;
    private String blockName;
    private LocalTime startTime;
    private LocalTime endTime;
    private SessionType sessionType;
    private String periods;
    private LocalDateTime createdAt;

    // ===== ENUMS =====
    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public enum SessionType {
        MORNING, AFTERNOON
    }

    // ===== CONSTRUCTORS =====
    public TimeSlot() {}

    public TimeSlot(DayOfWeek dayOfWeek, int blockNumber, String blockName, 
                    LocalTime startTime, LocalTime endTime, SessionType sessionType, String periods) {
        this.dayOfWeek = dayOfWeek;
        this.blockNumber = blockNumber;
        this.blockName = blockName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionType = sessionType;
        this.periods = periods;
    }

    // ===== GETTERS & SETTERS =====

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public int getBlockNumber() { return blockNumber; }
    public void setBlockNumber(int blockNumber) { this.blockNumber = blockNumber; }

    public String getBlockName() { return blockName; }
    public void setBlockName(String blockName) { this.blockName = blockName; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public SessionType getSessionType() { return sessionType; }
    public void setSessionType(SessionType sessionType) { this.sessionType = sessionType; }

    public String getPeriods() { return periods; }
    public void setPeriods(String periods) { this.periods = periods; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ===== EXTRA HELPERS =====

    public String getDisplayName() {
        // Ví dụ: MONDAY - Ca 1 (07:00 - 09:35)
        return dayOfWeek + " - " + blockName + " (" + startTime + " - " + endTime + ")";
    }

    public boolean isMorning() {
        return sessionType == SessionType.MORNING;
    }

    public boolean isAfternoon() {
        return sessionType == SessionType.AFTERNOON;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "slotId=" + slotId +
                ", dayOfWeek=" + dayOfWeek +
                ", blockNumber=" + blockNumber +
                ", blockName='" + blockName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", sessionType=" + sessionType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return slotId == timeSlot.slotId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(slotId);
    }
}