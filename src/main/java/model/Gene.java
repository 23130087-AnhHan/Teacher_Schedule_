package model;

/**
 * Gene - Đại diện cho 1 phân công trong chromosome
 * Mỗi gene = 1 teaching assignment được gán vào (room, slot)
 */
public class Gene {
    private TeachingAssignment assignment;
    private Room room;
    private TimeSlot timeSlot;

    // Constructors
    public Gene() {
    }

    public Gene(TeachingAssignment assignment, Room room, TimeSlot timeSlot) {
        this.assignment = assignment;
        this.room = room;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public TeachingAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(TeachingAssignment assignment) {
        this.assignment = assignment;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    // Helper methods
    public int getTeacherId() {
        return assignment.getTeacherId();
    }

    public int getAssignmentId() {
        return assignment.getAssignmentId();
    }

    public int getRoomId() {
        return room.getRoomId();
    }

    public int getSlotId() {
        return timeSlot.getSlotId();
    }

    public boolean isValid() {
        return assignment != null && room != null && timeSlot != null;
    }

    /**
     * Tạo Schedule object từ gene này
     */
    public Schedule toSchedule() {
        if (!isValid()) {
            throw new IllegalStateException("Gene không hợp lệ, không thể tạo Schedule");
        }

        Schedule schedule = new Schedule();
        schedule.setAssignmentId(assignment.getAssignmentId());
        schedule.setClassId(assignment.getClassId());
        schedule.setTeacherId(assignment.getTeacherId());
        schedule.setSubjectId(assignment.getSubjectId());
        schedule.setRoomId(room.getRoomId());
        schedule.setSlotId(timeSlot.getSlotId());
        schedule.setAssignmentType(assignment.getAssignmentType());
        schedule.setGroupName(assignment.getGroupName());
        schedule.setApplicableWeeks(assignment.getWeekRange());
        schedule.setSemester(assignment.getSemester());
        schedule.setAcademicYear(assignment.getAcademicYear());

        return schedule;
    }

    /**
     * Clone gene
     */
    public Gene clone() {
        return new Gene(this.assignment, this.room, this.timeSlot);
    }

    @Override
    public String toString() {
        return "Gene{" +
                "assignmentId=" + (assignment != null ? assignment.getAssignmentId() : "null") +
                ", teacherId=" + (assignment != null ?  assignment.getTeacherId() : "null") +
                ", roomId=" + (room != null ?  room.getRoomId() : "null") +
                ", slotId=" + (timeSlot != null ? timeSlot.getSlotId() : "null") +
                '}';
    }
}