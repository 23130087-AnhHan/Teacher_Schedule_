<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
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
    <style>
        .cell-practice {
            background: #fff7e6 !important;
            border-left: 4px solid #fd7e14;
        }
        .cell-theory {
            background: #e3f2fd !important;
            border-left: 4px solid #2196f3;
        }
        .schedule-tooltip {
            font-size: 0.93em;
        }
    </style>
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
                        <table class="table table-bordered table-striped align-middle">
                            <thead class="table-primary">
                                <tr>
                                    <th>#</th>
                                    <th>Môn học / Loại</th>
                                    <th>Nhóm / Tổ</th>
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
                                    String assignmentType = dto.getAssignmentType();
                                    assignmentType = (assignmentType == null) ? "" : assignmentType.trim();
                                    String typeClass = assignmentType.equalsIgnoreCase("PRACTICE") ? "cell-practice" : "cell-theory";
                                    String labelType = assignmentType.equalsIgnoreCase("PRACTICE") ? "Thực hành" : "Lý thuyết";
                                    String groupStr = (dto.getGroupName() != null && !dto.getGroupName().trim().isEmpty()) ? dto.getGroupName() : "-";
                                    // Tooltip info
                                    String tooltip = String.format("Môn: %s (%s)\nLoại: %s\nNhóm/Tổ: %s\nLớp: %s\nSL SV: %d\nPhòng: %s\nGiáo viên: %s\nThứ: %s - %s\nGiờ: %s-%s",
                                        dto.getSubjectName(), dto.getSubjectCode(), labelType, groupStr, dto.getClassCode(), dto.getNumStudents(),
                                        dto.getRoomCode(), dto.getTeacherName(), dto.getDayOfWeek(), dto.getBlockName(), dto.getStartTime(), dto.getEndTime()
                                    );
                            %>
                                <tr class="<%= typeClass %>">
                                    <td><%= index++ %></td>
                                    <td>
                                        <strong><%= dto.getSubjectCode() %></strong> - <%= dto.getSubjectName() %>
                                        <span class="badge bg-secondary"><%= labelType %></span>
                                    </td>
                                    <td><span class="badge bg-info text-dark"><%= groupStr %></span></td>
                                    <td><%= dto.getTeacherName() %></td>
                                    <td><%= dto.getClassCode() %></td>
                                    <td><%= dto.getRoomCode() %></td>
                                    <td><%= dto.getDayOfWeek() %></td>
                                    <td><%= dto.getBlockName() %></td>
                                    <td>
                                        <span title="<%= tooltip.replaceAll("\"", "'").replaceAll("\n", "&#10;") %>" data-bs-toggle="tooltip">
                                            <%= dto.getStartTime() %> - <%= dto.getEndTime() %>
                                        </span>
                                    </td>
                                    <td>
                                        <%= dto.getNumStudents() %>
                                        <% if (assignmentType.equalsIgnoreCase("PRACTICE")) { %>
                                            <span class="badge bg-warning text-dark">TH</span>
                                        <% } else { %>
                                            <span class="badge bg-primary">LT</span>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                    
                    <p class="text-muted">Tổng số:  <%= schedules.size() %> lịch</p>
                    
                <% } else { %>
                    <div class="alert alert-warning">
                        <h4>⚠️ Chưa có lịch</h4>
                        <p>Chưa có lịch cho học kỳ này.  Vui lòng chạy thuật toán để tạo lịch!</p>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Kích hoạt tooltip cho các dòng lịch
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
          return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    </script>
</body>
</html>