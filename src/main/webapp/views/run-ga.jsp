<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();

    Integer generations = (Integer) request.getAttribute("generations");
    Double fitness = (Double) request.getAttribute("fitness");
    Integer hardViolations = (Integer) request.getAttribute("hardViolations");
    Integer softViolations = (Integer) request.getAttribute("softViolations");
    Integer scheduleCount = (Integer) request.getAttribute("scheduleCount");

    String semesterShow = request.getParameter("semester");
    if (semesterShow == null) semesterShow = (String) request.getAttribute("semester");
    if (semesterShow == null) semesterShow = "HK1";

    String academicYearShow = request.getParameter("academicYear");
    if (academicYearShow == null) academicYearShow = (String) request.getAttribute("academicYear");
    if (academicYearShow == null) academicYearShow = "2025-2026";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chạy Genetic Algorithm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<%= contextPath %>/">🧬 Xếp Lịch GA</a>
        </div>
    </nav>

    <div class="container mt-5 mb-5">
        <div class="row">
            <div class="col-md-6 offset-md-3">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4>🚀 Chạy Genetic Algorithm</h4>
                    </div>
                    <div class="card-body">
                        <% if (generations == null) { %>
                        <!-- Form khởi chạy: GA sẽ tự chạy đến khi tối ưu/dừng -->
                        <form method="POST" action="<%= contextPath %>/run-ga">
                            <div class="mb-3">
                                <label class="form-label">Học kỳ</label>
                                <select name="semester" class="form-select" required>
                                    <option value="HK1" <%= ("HK1".equals(semesterShow) ? "selected" : "") %>>Học kỳ 1</option>
                                    <option value="HK2" <%= ("HK2".equals(semesterShow) ? "selected" : "") %>>Học kỳ 2</option>
                                    <option value="HK3" <%= ("HK3".equals(semesterShow) ? "selected" : "") %>>Học kỳ 3 (Hè)</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Năm học</label>
                                <input type="text" name="academicYear" class="form-control"
                                       value="<%= academicYearShow %>" required>
                            </div>
                            <div class="alert alert-info">
                                <strong>📝 Lưu ý:</strong> GA sẽ tự động tiến hóa (lai ghép/đột biến) và in tiến trình ra console từng thế hệ.
                                Sau khi kết thúc, giao diện hiển thị thống kê của lịch tối ưu nhất và bạn có thể xem lịch chi tiết.
                            </div>
                            <button type="submit" class="btn btn-primary w-100">
                                🚀 Chạy Thuật Toán
                            </button>
                        </form>
                        <% } else { %>
                        <!-- Kết quả cuối cùng -->
                        <% if (hardViolations != null && hardViolations > 0) { %>
                        <div class="alert alert-warning mb-3">
                            <strong>⚠️ Có Vi Phạm</strong><br>
                            Lịch có <b><%= hardViolations %></b> vi phạm nghiêm trọng.
                        </div>
                        <% } else { %>
                        <div class="alert alert-success mb-3">
                            <strong>✅ Lịch đã xếp hợp lệ!</strong>
                        </div>
                        <% } %>

                        <div class="mb-3">
                            <table class="table table-bordered">
                                <tr><th>Số lịch đã tạo</th><td><%= scheduleCount != null ? scheduleCount : 0 %></td></tr>
                                <tr><th>Học kỳ</th><td><%= semesterShow %></td></tr>
                                <tr><th>Năm học</th><td><%= academicYearShow %></td></tr>
                                <tr><th>Số thế hệ</th><td><%= generations != null ? generations : 0 %></td></tr>
                                <tr><th>Fitness tốt nhất</th><td><%= fitness != null ? fitness : 0 %></td></tr>
                                <tr><th>Hard violations</th><td><%= hardViolations != null ? hardViolations : 0 %></td></tr>
                                <tr><th>Soft violations</th><td><%= softViolations != null ? softViolations : 0 %></td></tr>
                            </table>
                        </div>

                        <a href="<%= contextPath %>/schedule-list?semester=<%= semesterShow %>&academicYear=<%= academicYearShow %>"
                           class="btn btn-success w-100 mt-1">
                            📅 Xem Lịch Vừa Tạo
                        </a>
                        <% } %>
                    </div>
                </div>
                <div class="mt-3">
                    <a href="<%= contextPath %>/" class="btn btn-secondary">← Quay lại</a>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>