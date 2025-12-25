package service;

import dao.*;
import model.*;
import java.util.*;

public class ScheduleService {
    
    private ScheduleDAO scheduleDAO;
    private TeacherDAO teacherDAO;
    private SubjectDAO subjectDAO;
    private RoomDAO roomDAO;
    private TimeSlotDAO timeSlotDAO;
    private CourseClassDAO classDAO;
    
    public ScheduleService() {
        this.scheduleDAO = new ScheduleDAO();
        this.teacherDAO = new TeacherDAO();
        this.subjectDAO = new SubjectDAO();
        this.roomDAO = new RoomDAO();
        this.timeSlotDAO = new TimeSlotDAO();
        this.classDAO = new CourseClassDAO();
    }
    
    /**
     * Lấy lịch theo học kỳ (với thông tin đầy đủ)
     */
    public List<ScheduleDetailDTO> getScheduleDetails(String semester, String academicYear) {
        List<Schedule> schedules = scheduleDAO. getSchedulesBySemester(semester, academicYear);
        List<ScheduleDetailDTO> details = new ArrayList<>();
        
        for (Schedule schedule : schedules) {
            ScheduleDetailDTO dto = new ScheduleDetailDTO();
            
            // Schedule info
            dto.setSchedule(schedule);
            
            // Teacher info
            Teacher teacher = teacherDAO.getTeacherById(schedule.getTeacherId());
            dto.setTeacherName(teacher != null ? teacher.getFullName() : "Unknown");
            dto.setTeacherCode(teacher != null ? teacher.getTeacherCode() : "");
            
            // Subject info
            Subject subject = subjectDAO. getSubjectById(schedule.getSubjectId());
            dto.setSubjectName(subject != null ? subject.getSubjectName() : "Unknown");
            dto.setSubjectCode(subject != null ? subject.getSubjectCode() : "");
            
            // Room info
            Room room = roomDAO.getRoomById(schedule. getRoomId());
            dto.setRoomCode(room != null ? room.getRoomCode() : "Unknown");
            dto.setRoomCapacity(room != null ? room. getCapacity() : 0);
            
            // TimeSlot info
            TimeSlot timeSlot = timeSlotDAO.getTimeSlotById(schedule.getSlotId());
            dto.setDayOfWeek(timeSlot != null ? timeSlot.getDayOfWeek().toString() : "");
            dto.setBlockName(timeSlot != null ? timeSlot.getBlockName() : "");
            dto.setStartTime(timeSlot != null ?  timeSlot.getStartTime().toString() : "");
            dto.setEndTime(timeSlot != null ? timeSlot.getEndTime().toString() : "");
            
            // Class info
            CourseClass courseClass = classDAO.getCourseClassById(schedule.getClassId());
            dto.setClassCode(courseClass != null ? courseClass.getClassCode() : "");
            dto.setNumStudents(courseClass != null ?  courseClass.getTotalStudents() : 0);
            
            details.add(dto);
        }
        
        return details;
    }
    
    /**
     * Lấy lịch theo giáo viên
     */
    public List<ScheduleDetailDTO> getScheduleByTeacher(int teacherId, String semester, String academicYear) {
        List<Schedule> schedules = scheduleDAO.getSchedulesByTeacher(teacherId, semester, academicYear);
        return convertToDetails(schedules);
    }
    
    /**
     * Lấy lịch theo phòng
     */
    public List<ScheduleDetailDTO> getScheduleByRoom(int roomId, String semester, String academicYear) {
        List<Schedule> schedules = scheduleDAO. getSchedulesByRoom(roomId, semester, academicYear);
        return convertToDetails(schedules);
    }
    
    /**
     * Lấy lịch dạng bảng (grid) theo tuần
     */
    public Map<String, Map<String, List<ScheduleDetailDTO>>> getWeeklyScheduleGrid(String semester, String academicYear) {
        // Map<DayOfWeek, Map<BlockName, List<ScheduleDetailDTO>>>
        Map<String, Map<String, List<ScheduleDetailDTO>>> grid = new LinkedHashMap<>();
        
        List<ScheduleDetailDTO> allSchedules = getScheduleDetails(semester, academicYear);
        
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        String[] blocks = {"Sáng 1", "Sáng 2", "Chiều 1", "Chiều 2"};
        
        for (String day : days) {
            Map<String, List<ScheduleDetailDTO>> daySchedules = new LinkedHashMap<>();
            for (String block : blocks) {
                daySchedules.put(block, new ArrayList<>());
            }
            grid.put(day, daySchedules);
        }
        
        for (ScheduleDetailDTO dto : allSchedules) {
            String day = dto.getDayOfWeek();
            String block = dto.getBlockName();
            
            if (grid.containsKey(day) && grid.get(day).containsKey(block)) {
                grid.get(day).get(block).add(dto);
            }
        }
        
        return grid;
    }
    
    private List<ScheduleDetailDTO> convertToDetails(List<Schedule> schedules) {
        // Similar to getScheduleDetails() but for existing schedules list
        // (implementation similar to above)
        return new ArrayList<>(); // TODO: implement
    }
}