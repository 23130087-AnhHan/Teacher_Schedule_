package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng teaching_assignments
 * ĐÂY LÀ INPUT CHÍNH CHO THUẬT TOÁN DI TRUYỀN
 */
public class TeachingAssignment {
    private int assignmentId;
    private int classId;
    private int teacherId;
    private int subjectId;
    private AssignmentType assignmentType;
    private String groupName;
    private int numStudents;
    private int startWeek;
    private int endWeek;
    private int totalWeeks;  // Generated column
    private int blocksPerWeek;
    private boolean requiresLab;
    private int minRoomCapacity;
    private String semester;
    private String academicYear;
    private LocalDateTime createdAt;

    // Enum
    public enum AssignmentType {
        THEORY, PRACTICE
    }

    // Constructors
    public TeachingAssignment() {
    }

    public TeachingAssignment(int classId, int teacherId, int subjectId, AssignmentType assignmentType,
                              String groupName, int numStudents, int startWeek, int endWeek,
                              int blocksPerWeek, boolean requiresLab, int minRoomCapacity,
                              String semester, String academicYear) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this. assignmentType = assignmentType;
        this.groupName = groupName;
        this.numStudents = numStudents;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.totalWeeks = endWeek - startWeek + 1;
        this.blocksPerWeek = blocksPerWeek;
        this.requiresLab = requiresLab;
        this. minRoomCapacity = minRoomCapacity;
        this. semester = semester;
        this. academicYear = academicYear;
    }

    // Getters and Setters
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

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
        this.totalWeeks = this. endWeek - startWeek + 1;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
        this.totalWeeks = endWeek - this.startWeek + 1;
    }

    public int getTotalWeeks() {
        return totalWeeks;
    }

    public void setTotalWeeks(int totalWeeks) {
        this.totalWeeks = totalWeeks;
    }

    public int getBlocksPerWeek() {
        return blocksPerWeek;
    }

    public void setBlocksPerWeek(int blocksPerWeek) {
        this.blocksPerWeek = blocksPerWeek;
    }

    public boolean isRequiresLab() {
        return requiresLab;
    }

    public void setRequiresLab(boolean requiresLab) {
        this.requiresLab = requiresLab;
    }

    public int getMinRoomCapacity() {
        return minRoomCapacity;
    }

    public void setMinRoomCapacity(int minRoomCapacity) {
        this.minRoomCapacity = minRoomCapacity;
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

    // Helper methods
    public boolean isTheory() {
        return assignmentType == AssignmentType.THEORY;
    }

    public boolean isPractice() {
        return assignmentType == AssignmentType.PRACTICE;
    }

    /**
     * Tổng số ca cần xếp cho assignment này
     */
    public int getTotalBlocksNeeded() {
        return totalWeeks * blocksPerWeek;
    }

    /**
     * Format tuần học (VD: "1-10", "3-12")
     */
    public String getWeekRange() {
        return startWeek + "-" + endWeek;
    }

    @Override
    public String toString() {
        return "TeachingAssignment{" +
                "assignmentId=" + assignmentId +
                ", classId=" + classId +
                ", teacherId=" + teacherId +
                ", assignmentType=" + assignmentType +
                ", groupName='" + groupName + '\'' +
                ", numStudents=" + numStudents +
                ", weeks=" + startWeek + "-" + endWeek +
                ", requiresLab=" + requiresLab +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeachingAssignment that = (TeachingAssignment) o;
        return assignmentId == that.assignmentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(assignmentId);
    }
}