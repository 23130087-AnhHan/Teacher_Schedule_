package service;

import model.Schedule;

/**
 * DTO chứa thông tin đầy đủ của 1 schedule entry
 * (để hiển thị trên UI)
 */
public class ScheduleDetailDTO {
    private Schedule schedule;

    // NEW: Thể hiện loại lịch (THEORY/PRACTICE)
    private String assignmentType; // "THEORY" hoặc "PRACTICE"

    // NEW: Nhóm/tổ (TH01/LT02/...)
    private String groupName;

    // Teacher info
    private String teacherCode;
    private String teacherName;

    // Subject info
    private String subjectCode;
    private String subjectName;

    // Room info
    private String roomCode;
    private int roomCapacity;

    // TimeSlot info
    private String dayOfWeek;
    private String blockName;
    private String startTime;
    private String endTime;

    // Class info
    private String classCode;
    private int numStudents;

    // Constructors
    public ScheduleDetailDTO() {}

    // Getters and Setters
    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public String getAssignmentType() { return assignmentType; }
    public void setAssignmentType(String assignmentType) { this.assignmentType = assignmentType; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getTeacherCode() { return teacherCode; }
    public void setTeacherCode(String teacherCode) { this.teacherCode = teacherCode; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public int getRoomCapacity() { return roomCapacity; }
    public void setRoomCapacity(int roomCapacity) { this.roomCapacity = roomCapacity; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getBlockName() { return blockName; }
    public void setBlockName(String blockName) { this.blockName = blockName; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public int getNumStudents() { return numStudents; }
    public void setNumStudents(int numStudents) { this.numStudents = numStudents; }

    /**
     * Hiệu quả: true nếu là thực hành, false nếu là lý thuyết
     */
    
    public boolean isPractice() {
        return assignmentType != null && assignmentType.equalsIgnoreCase("PRACTICE");
    }

    public boolean isTheory() {
        return assignmentType != null && assignmentType.equalsIgnoreCase("THEORY");
    }

    /**
     * Format thành 1 dòng để hiển thị
     */
    public String getDisplayString() {
        return String.format("%s - %s\n%s (%s)\n%s | %d SV | %s | %s",
            subjectCode, subjectName,
            teacherName, classCode,
            roomCode, numStudents, assignmentType, groupName);
    }

    @Override
    public String toString() {
        return getDisplayString();
    }
}	