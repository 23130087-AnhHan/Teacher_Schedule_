package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng schedules
 * ĐÂY LÀ OUTPUT CỦA THUẬT TOÁN DI TRUYỀN
 */
public class Schedule {
    private int scheduleId;
    private int assignmentId;
    private int classId;
    private int teacherId;
    private int subjectId;
    private int roomId;
    private int slotId;
    private TeachingAssignment. AssignmentType assignmentType;
    private String groupName;
    private String applicableWeeks;
    private String semester;
    private String academicYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Schedule() {
    }

    public Schedule(int assignmentId, int classId, int teacherId, int subjectId,
                    int roomId, int slotId, TeachingAssignment.AssignmentType assignmentType,
                    String groupName, String applicableWeeks, String semester, String academicYear) {
        this.assignmentId = assignmentId;
        this.classId = classId;
        this. teacherId = teacherId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this. slotId = slotId;
        this.assignmentType = assignmentType;
        this. groupName = groupName;
        this.applicableWeeks = applicableWeeks;
        this. semester = semester;
        this. academicYear = academicYear;
    }

    // Getters and Setters
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public TeachingAssignment.AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(TeachingAssignment.AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getApplicableWeeks() {
        return applicableWeeks;
    }

    public void setApplicableWeeks(String applicableWeeks) {
        this.applicableWeeks = applicableWeeks;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", assignmentId=" + assignmentId +
                ", teacherId=" + teacherId +
                ", roomId=" + roomId +
                ", slotId=" + slotId +
                ", applicableWeeks='" + applicableWeeks + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return scheduleId == schedule.scheduleId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(scheduleId);
    }
}
