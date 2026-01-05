package service;

import dao.*;
import model.*;
import java.util.*;

/**
 * Service xử lý nghiệp vụ lịch dạy cho hệ thống xếp lịch GA
 */
public class ScheduleService {

    private ScheduleDAO scheduleDAO;
    private TeacherDAO teacherDAO;
    private SubjectDAO subjectDAO;
    private RoomDAO roomDAO;
    private TimeSlotDAO timeSlotDAO;
    private CourseClassDAO classDAO;
    private TeachingAssignmentDAO assignmentDAO;

    public ScheduleService() {
        this.scheduleDAO = new ScheduleDAO();
        this.teacherDAO = new TeacherDAO();
        this.subjectDAO = new SubjectDAO();
        this.roomDAO = new RoomDAO();
        this.timeSlotDAO = new TimeSlotDAO();
        this.classDAO = new CourseClassDAO();
        this.assignmentDAO = new TeachingAssignmentDAO();
    }

    /** Lấy lịch tổng thể học kỳ */
    public List<ScheduleDetailDTO> getScheduleDetails(String semester, String academicYear) {
        List<Schedule> schedules = scheduleDAO.getSchedulesBySemester(semester, academicYear);
        return convertToDetails(schedules);
    }

    /** Lấy lịch của tất cả giáo viên */
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
    }

    /** Lấy lịch của một giáo viên */
    public List<ScheduleDetailDTO> getScheduleByTeacher(int teacherId, String semester, String academicYear) {
        List<Schedule> schedules = scheduleDAO.getSchedulesByTeacher(teacherId, semester, academicYear);
        return convertToDetails(schedules);
    }

    /** Lấy lịch của một phòng học */
    public List<ScheduleDetailDTO> getScheduleByRoom(int roomId, String semester, String academicYear) {
        List<Schedule> schedules = scheduleDAO.getSchedulesByRoom(roomId, semester, academicYear);
        return convertToDetails(schedules);
    }

    /** Convert danh sách Schedule sang ScheduleDetailDTO
     * Đầy đủ: assignmentType (lý thuyết/thực hành), groupName (nhóm/tổ), sĩ số, giáo viên, môn, phòng, ca, lớp!
     */
    private List<ScheduleDetailDTO> convertToDetails(List<Schedule> schedules) {
        List<ScheduleDetailDTO> details = new ArrayList<>();

        for (Schedule schedule : schedules) {
            ScheduleDetailDTO dto = new ScheduleDetailDTO();

            // Gán model gốc
            dto.setSchedule(schedule);

            // AssignmentType + GroupName (lý thuyết/thực hành + tổ/nhóm)
            dto.setAssignmentType(
                schedule.getAssignmentType() != null ? schedule.getAssignmentType().name() : ""
            );
            dto.setGroupName(
                schedule.getGroupName() != null ? schedule.getGroupName() : ""
            );

            // Giáo viên
            Teacher teacher = teacherDAO.getTeacherById(schedule.getTeacherId());
            dto.setTeacherName(teacher != null ? teacher.getFullName() : "Unknown");
            dto.setTeacherCode(teacher != null ? teacher.getTeacherCode() : "");

            // Môn học
            Subject subject = subjectDAO.getSubjectById(schedule.getSubjectId());
            dto.setSubjectName(subject != null ? subject.getSubjectName() : "Unknown");
            dto.setSubjectCode(subject != null ? subject.getSubjectCode() : "");

            // Phòng học
            Room room = roomDAO.getRoomById(schedule.getRoomId());
            dto.setRoomCode(room != null ? room.getRoomCode() : "Unknown");
            dto.setRoomCapacity(room != null ? room.getCapacity() : 0);

            // Ca/Thời gian
            TimeSlot timeSlot = timeSlotDAO.getTimeSlotById(schedule.getSlotId());
            dto.setDayOfWeek(timeSlot != null ? timeSlot.getDayOfWeek().toString() : "");
            dto.setBlockName(timeSlot != null ? timeSlot.getBlockName() : "");
            dto.setStartTime(timeSlot != null ? timeSlot.getStartTime().toString() : "");
            dto.setEndTime(timeSlot != null ? timeSlot.getEndTime().toString() : "");

            // Lớp học
            CourseClass courseClass = classDAO.getCourseClassById(schedule.getClassId());
            dto.setClassCode(courseClass != null ? courseClass.getClassCode() : "");

            // Sĩ số: Đúng cho từng tổ hoặc nhóm
            TeachingAssignment assignment = assignmentDAO.getAssignmentById(schedule.getAssignmentId());
            if (assignment != null && assignment.getNumStudents() > 0) {
                dto.setNumStudents(assignment.getNumStudents());
            } else {
                dto.setNumStudents(courseClass != null ? courseClass.getTotalStudents() : 0);
            }
            // Sĩ số luôn đúng: PRACTICE là số SV tổ, THEORY là số SV nhóm/lớp

            details.add(dto);
        }
        return details;
    }
}