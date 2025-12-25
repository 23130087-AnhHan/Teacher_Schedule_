package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng teacher_subjects (Many-to-Many relationship)
 */
public class TeacherSubject {
    private int teacherId;
    private int subjectId;
    private boolean canTeachTheory;
    private boolean canTeachPractice;
    private int priority;
    private LocalDateTime createdAt;

    // Constructors
    public TeacherSubject() {
    }

    public TeacherSubject(int teacherId, int subjectId, boolean canTeachTheory, 
                          boolean canTeachPractice, int priority) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.canTeachTheory = canTeachTheory;
        this.canTeachPractice = canTeachPractice;
        this. priority = priority;
    }

    // Getters and Setters
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

    public boolean isCanTeachTheory() {
        return canTeachTheory;
    }

    public void setCanTeachTheory(boolean canTeachTheory) {
        this.canTeachTheory = canTeachTheory;
    }

    public boolean isCanTeachPractice() {
        return canTeachPractice;
    }

    public void setCanTeachPractice(boolean canTeachPractice) {
        this.canTeachPractice = canTeachPractice;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TeacherSubject{" +
                "teacherId=" + teacherId +
                ", subjectId=" + subjectId +
                ", canTeachTheory=" + canTeachTheory +
                ", canTeachPractice=" + canTeachPractice +
                ", priority=" + priority +
                '}';
    }
}