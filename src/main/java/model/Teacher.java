package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng teachers
 */
public class Teacher {
    private int teacherId;
    private String teacherCode;
    private String fullName;
    private String department;
    private int maxTeachingHours;
    private LocalDateTime createdAt;

    // Constructors
    public Teacher() {
    }

    public Teacher(String teacherCode, String fullName, String department, int maxTeachingHours) {
        this.teacherCode = teacherCode;
        this.fullName = fullName;
        this.department = department;
        this.maxTeachingHours = maxTeachingHours;
    }

    // Getters and Setters
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getMaxTeachingHours() {
        return maxTeachingHours;
    }

    public void setMaxTeachingHours(int maxTeachingHours) {
        this.maxTeachingHours = maxTeachingHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", teacherCode='" + teacherCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", department='" + department + '\'' +
                ", maxTeachingHours=" + maxTeachingHours +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return teacherId == teacher.teacherId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(teacherId);
    }
}
