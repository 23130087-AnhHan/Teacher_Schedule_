package controller;

import dao.TeacherDAO;
import model.Teacher;
import service. ScheduleService;
import service. ScheduleDetailDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/teacher-schedule")
public class TeacherScheduleServlet extends HttpServlet {
    
    private ScheduleService scheduleService = new ScheduleService();
    private TeacherDAO teacherDAO = new TeacherDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String semester = request.getParameter("semester");
        String academicYear = request.getParameter("academicYear");
        
        // Default values
        if (semester == null) semester = "HK1";
        if (academicYear == null) academicYear = "2025-2026";
        
        // Lấy tất cả giáo viên
        List<Teacher> allTeachers = teacherDAO. getAllTeachers();
        
        // Tạo Map:  teacherId -> Lịch tuần của GV đó
        Map<Teacher, Map<String, Map<String, List<ScheduleDetailDTO>>>> teacherSchedules = new LinkedHashMap<>();
        
        for (Teacher teacher : allTeachers) {
            // Lấy lịch của GV này
            List<ScheduleDetailDTO> schedules = scheduleService.getScheduleByTeacher(
                teacher.getTeacherId(), semester, academicYear
            );
            
            // Nếu GV có lịch
            if (!schedules.isEmpty()) {
                // Tạo lịch dạng grid (tuần)
                Map<String, Map<String, List<ScheduleDetailDTO>>> grid = createWeeklyGrid(schedules);
                teacherSchedules.put(teacher, grid);
            }
        }
        
        // Gửi data sang JSP
        request.setAttribute("teacherSchedules", teacherSchedules);
        request.setAttribute("semester", semester);
        request.setAttribute("academicYear", academicYear);
        
        request.getRequestDispatcher("/views/teacher-schedule-weekly.jsp").forward(request, response);
    }
    
    /**
     * Tạo lịch dạng bảng tuần
     * Map<DayOfWeek, Map<BlockName, List<ScheduleDetailDTO>>>
     */
    private Map<String, Map<String, List<ScheduleDetailDTO>>> createWeeklyGrid(List<ScheduleDetailDTO> schedules) {
        Map<String, Map<String, List<ScheduleDetailDTO>>> grid = new LinkedHashMap<>();
        
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        String[] blocks = {"Sáng 1", "Sáng 2", "Chiều 1", "Chiều 2"};
        
        // Khởi tạo grid rỗng
        for (String day : days) {
            Map<String, List<ScheduleDetailDTO>> daySchedules = new LinkedHashMap<>();
            for (String block : blocks) {
                daySchedules.put(block, new ArrayList<>());
            }
            grid.put(day, daySchedules);
        }
        
        // Điền dữ liệu
        for (ScheduleDetailDTO dto : schedules) {
            String day = dto.getDayOfWeek();
            String block = dto.getBlockName();
            
            if (grid.containsKey(day) && grid.get(day).containsKey(block)) {
                grid.get(day).get(block).add(dto);
            }
        }
        
        return grid;
    }
}