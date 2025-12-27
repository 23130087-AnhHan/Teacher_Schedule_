<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Teacher" %>
<%@ page import="service.ScheduleDetailDTO" %>
<%
    String contextPath = request.getContextPath();
    Map<Teacher, Map<String, Map<String, List<ScheduleDetailDTO>>>> teacherSchedules = 
        (Map<Teacher, Map<String, Map<String, List<ScheduleDetailDTO>>>>) request.getAttribute("teacherSchedules");
    String semester = (String) request.getAttribute("semester");
    String academicYear = (String) request.getAttribute("academicYear");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lịch Giảng Dạy Theo Giáo Viên</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .teacher-section {
            margin-bottom: 50px;
            page-break-after: always;
        }
        .teacher-header {
            background:  linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding:  20px;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .schedule-cell {
            min-height: 80px;
            vertical-align: top;
            font-size: 0.85rem;
            padding: 8px;
        }
        /*--- Cũ cho lịch lý thuyết ---*/
        .schedule-item.theory {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
            padding:  8px;
            margin-bottom: 8px;
            border-radius: 4px;
            color: #1976d2;
            font-weight: 400;
        }
        /*--- Mới, nổi bật cho lịch thực hành ---*/
        .schedule-item.practice {
            background: #fff1f0;
            border-left: 4px solid #d32f2f;
            padding:  8px;
            margin-bottom: 8px;
            border-radius: 4px;
            color: #b71c1c;
            font-weight: 500;
        }
        .schedule-item strong {
            display: block;
            margin-bottom: 4px;
        }
        .schedule-item .badge {
            font-size: 0.78em;
            vertical-align: middle;
        }
        .empty-cell {
            background: #f5f5f5;
        }
        @media print {
            .no-print {
                display: none;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary no-print">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%= contextPath %>/">🧬 Xếp Lịch GA</a>
        </div>
    </nav>

    <div class="container-fluid mt-4">
        <div class="row no-print">
            <div class="col-md-12">
                <h2>📅 Lịch Giảng Dạy Theo Giáo Viên</h2>
                <p>Học kỳ: <strong><%= semester %> <%= academicYear %></strong></p>
                <button onclick="window.print()" class="btn btn-success mb-3">🖨️ In Lịch</button>
                <a href="<%= contextPath %>/" class="btn btn-secondary mb-3">← Trang Chủ</a>
                <hr>
            </div>
        </div>

        <% 
        if (teacherSchedules != null && ! teacherSchedules.isEmpty()) {
            String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
            String[] dayNames = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
            String[] blocks = {"Ca 1", "Ca 2", "Ca 3", "Ca 4"};
            
            int teacherIndex = 1;
            for (Map.Entry<Teacher, Map<String, Map<String, List<ScheduleDetailDTO>>>> entry : teacherSchedules.entrySet()) {
                Teacher teacher = entry.getKey();
                Map<String, Map<String, List<ScheduleDetailDTO>>> grid = entry.getValue();
        %>
        
        <!-- ================================ -->
        <!-- BẢNG LỊCH CỦA MỘT GIÁO VIÊN -->
        <!-- ================================ -->
        <div class="teacher-section">
            <div class="teacher-header">
                <h3>👨‍🏫 <%= teacherIndex++ %>. <%= teacher.getFullName() %></h3>
                <p class="mb-0">Mã GV: <strong><%= teacher.getTeacherCode() %></strong></p>
            </div>

            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="table-primary">
                        <tr>
                            <th style="width: 100px;">Tiết/Thứ</th>
                            <% for (String dayName : dayNames) { %>
                                <th><%= dayName %></th>
                            <% } %>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        for (String block : blocks) {
                        %>
                        <tr>
                            <td class="text-center align-middle"><strong><%= block %></strong></td>
                            <% 
                            for (String day :  days) {
                                List<ScheduleDetailDTO> schedules = grid.get(day).get(block);
                                boolean hasSchedule = schedules != null && !schedules. isEmpty();
                            %>
                            <td class="schedule-cell <%= ! hasSchedule ? "empty-cell" : "" %>">
                                <% 
                                if (hasSchedule) {
                                    for (ScheduleDetailDTO dto : schedules) {
                                        // Phân biệt loại lịch
                                        String typeClass = "theory";
                                        if ("PRACTICE".equalsIgnoreCase(dto.getAssignmentType())) {
                                            typeClass = "practice";
                                        }
                                %>
                                <div class="schedule-item <%= typeClass %>">
                                    <strong><%= dto.getSubjectCode() %></strong>
                                    <%= dto.getSubjectName() %>
                                    <% if ("PRACTICE".equalsIgnoreCase(dto.getAssignmentType())) { %>
                                        <span class="badge bg-danger ms-1">Thực hành</span>
                                    <% } else { %>
                                        <span class="badge bg-primary ms-1">Lý thuyết</span>
                                    <% } %>
                                    <br>
                                    <small>
                                        📍 <%= dto.getRoomCode() %> | 
                                        👥 <%= dto.getNumStudents() %> SV<br>
                                        🕐 <%= dto.getStartTime() %> - <%= dto.getEndTime() %><br>
                                        📚 <%= dto.getClassCode() %>
                                    </small>
                                </div>
                                <% 
                                    }
                                } else {
                                %>
                                <span class="text-muted">-</span>
                                <% } %>
                            </td>
                            <% } %>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <% 
            } // end for each teacher
        } else {
        %>
        <div class="alert alert-warning">
            <h4>⚠️ Chưa có lịch</h4>
            <p>Chưa có lịch cho học kỳ này.  Vui lòng chạy thuật toán để tạo lịch! </p>
            <a href="<%= contextPath %>/run-ga" class="btn btn-primary">🚀 Tạo Lịch Ngay</a>
        </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle. min.js"></script>
</body>
</html>