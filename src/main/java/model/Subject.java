package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng subjects
 */
public class Subject {
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private int totalCredits;
    private int theoryCredits;
    private int practiceCredits;
    private int maxStudentsPerPracticeGroup;
    private String department;
    private LocalDateTime createdAt;

    // Constructors
    public Subject() {
    }

    public Subject(String subjectCode, String subjectName, int totalCredits, 
                   int theoryCredits, int practiceCredits, int maxStudentsPerPracticeGroup) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.totalCredits = totalCredits;
        this.theoryCredits = theoryCredits;
        this.practiceCredits = practiceCredits;
        this.maxStudentsPerPracticeGroup = maxStudentsPerPracticeGroup;
    }

    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public int getTheoryCredits() {
        return theoryCredits;
    }

    public void setTheoryCredits(int theoryCredits) {
        this.theoryCredits = theoryCredits;
    }

    public int getPracticeCredits() {
        return practiceCredits;
    }

    public void setPracticeCredits(int practiceCredits) {
        this.practiceCredits = practiceCredits;
    }

    public int getMaxStudentsPerPracticeGroup() {
        return maxStudentsPerPracticeGroup;
    }

    public void setMaxStudentsPerPracticeGroup(int maxStudentsPerPracticeGroup) {
        this.maxStudentsPerPracticeGroup = maxStudentsPerPracticeGroup;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean hasPractice() {
        return practiceCredits > 0;
    }

    public boolean hasTheoryOnly() {
        return practiceCredits == 0;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", totalCredits=" + totalCredits +
                ", theoryCredits=" + theoryCredits +
                ", practiceCredits=" + practiceCredits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return subjectId == subject.subjectId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(subjectId);
    }
}