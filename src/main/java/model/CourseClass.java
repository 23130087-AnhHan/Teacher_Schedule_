package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng course_classes
 */
public class CourseClass {
    private int classId;
    private String classCode;
    private int subjectId;
    private int totalStudents;
    private String semester;
    private String academicYear;
    private int durationWeeks;
    private LocalDateTime createdAt;

    // Constructors
    public CourseClass() {
    }

    public CourseClass(String classCode, int subjectId, int totalStudents, 
                       String semester, String academicYear, int durationWeeks) {
        this.classCode = classCode;
        this.subjectId = subjectId;
        this.totalStudents = totalStudents;
        this.semester = semester;
        this.academicYear = academicYear;
        this.durationWeeks = durationWeeks;
    }

    // Getters and Setters
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
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

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CourseClass{" +
                "classId=" + classId +
                ", classCode='" + classCode + '\'' +
                ", subjectId=" + subjectId +
                ", totalStudents=" + totalStudents +
                ", semester='" + semester + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", durationWeeks=" + durationWeeks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseClass that = (CourseClass) o;
        return classId == that. classId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(classId);
    }
}