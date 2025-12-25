<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util. List" %>
<%@ page import="service.ScheduleDetailDTO" %>
<%
    String contextPath = request.getContextPath();
    List<ScheduleDetailDTO> schedules = (List<ScheduleDetailDTO>) request.getAttribute("schedules");
    String semester = (String) request.getAttribute("semester");
    String academicYear = (String) request.getAttribute("academicYear");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh Sách Lịch</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<%= contextPath %>/">🧬 Xếp Lịch GA</a>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-12">
                <h2>📅 Danh Sách Lịch</h2>
                <p>Học kỳ: <strong><%= semester %> <%= academicYear %></strong></p>
                
                <% if (schedules != null && !schedules.isEmpty()) { %>
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped">
                            <thead class="table-primary">
                                <tr>
                                    <th>#</th>
                                    <th>Môn học</th>
                                    <th>Giáo viên</th>
                                    <th>Lớp</th>
                                    <th>Phòng</th>
                                    <th>Thứ</th>
                                    <th>Tiết</th>
                                    <th>Giờ</th>
                                    <th>SL SV</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                int index = 1;
                                for (ScheduleDetailDTO dto : schedules) { 
                                %>
                                <tr>
                                    <td><%= index++ %></td>
                                    <td><%= dto.getSubjectCode() %> - <%= dto.getSubjectName() %></td>
                                    <td><%= dto.getTeacherName() %></td>
                                    <td><%= dto.getClassCode() %></td>
                                    <td><%= dto.getRoomCode() %></td>
                                    <td><%= dto.getDayOfWeek() %></td>
                                    <td><%= dto.getBlockName() %></td>
                                    <td><%= dto.getStartTime() %> - <%= dto.getEndTime() %></td>
                                    <td><%= dto.getNumStudents() %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    
                    <p class="text-muted">Tổng số:  <%= schedules.size() %> lịch</p>
                    
                <% } else { %>
                    <div class="alert alert-warning">
                        <h4>⚠️ Chưa có lịch</h4>
                        <p>Chưa có lịch cho học kỳ này.  Vui lòng chạy thuật toán để tạo lịch! </p>
                        <a href="<%= contextPath %>/run-ga" class="btn btn-primary">
                            🚀 Tạo Lịch Ngay
                        </a>
                    </div>
                <% } %>
                
                <div class="mt-3">
                    <a href="<%= contextPath %>/" class="btn btn-secondary">← Trang Chủ</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap. bundle.min.js"></script>
</body>
</html>