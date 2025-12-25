package controller;

import service.ScheduleService;
import service.ScheduleDetailDTO;


import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet {
    
    private ScheduleService scheduleService = new ScheduleService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String semester = request.getParameter("semester");
        String academicYear = request.getParameter("academicYear");
        
        if (action == null) action = "list";
        
        switch (action) {
            case "list":
                showScheduleList(request, response, semester, academicYear);
                break;
            case "teacher": 
                showScheduleByTeacher(request, response, semester, academicYear);
                break;
            case "room": 
                showScheduleByRoom(request, response, semester, academicYear);
                break;
            case "weekly":
                showWeeklySchedule(request, response, semester, academicYear);
                break;
            default:
                showScheduleList(request, response, semester, academicYear);
        }
    }
    
    private void showScheduleList(HttpServletRequest request, HttpServletResponse response, 
                                   String semester, String academicYear) 
            throws ServletException, IOException {
        
        List<ScheduleDetailDTO> schedules = scheduleService.getScheduleDetails(semester, academicYear);
        
        request.setAttribute("schedules", schedules);
        request.setAttribute("semester", semester);
        request.setAttribute("academicYear", academicYear);
        
        request.getRequestDispatcher("/views/schedule-list.jsp").forward(request, response);
    }
    
    private void showScheduleByTeacher(HttpServletRequest request, HttpServletResponse response,
                                        String semester, String academicYear) 
            throws ServletException, IOException {
        
        int teacherId = Integer.parseInt(request.getParameter("teacherId"));
        List<ScheduleDetailDTO> schedules = scheduleService.getScheduleByTeacher(teacherId, semester, academicYear);
        
        request.setAttribute("schedules", schedules);
        request.setAttribute("teacherId", teacherId);
        
        request.getRequestDispatcher("/views/schedule-teacher.jsp").forward(request, response);
    }
    
    private void showScheduleByRoom(HttpServletRequest request, HttpServletResponse response,
                                     String semester, String academicYear) 
            throws ServletException, IOException {
        
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        List<ScheduleDetailDTO> schedules = scheduleService.getScheduleByRoom(roomId, semester, academicYear);
        
        request.setAttribute("schedules", schedules);
        request.setAttribute("roomId", roomId);
        
        request.getRequestDispatcher("/views/schedule-room. jsp").forward(request, response);
    }
    
    private void showWeeklySchedule(HttpServletRequest request, HttpServletResponse response,
                                     String semester, String academicYear) 
            throws ServletException, IOException {
        
        var grid = scheduleService.getWeeklyScheduleGrid(semester, academicYear);
        
        request.setAttribute("scheduleGrid", grid);
        request.setAttribute("semester", semester);
        request.setAttribute("academicYear", academicYear);
        
        request.getRequestDispatcher("/views/schedule-weekly.jsp").forward(request, response);
    }
    
}