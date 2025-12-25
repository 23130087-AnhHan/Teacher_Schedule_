<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="algorithm.GeneticAlgorithm.GAResult" %>
<%@ page import="model. Chromosome" %>
<%
    GAResult result = (GAResult) request.getAttribute("result");
    String semester = (String) request.getAttribute("semester");
    String academicYear = (String) request.getAttribute("academicYear");
    Chromosome best = result != null ?  result.getBestChromosome() : null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kết Quả GA</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <!-- ✅ SỬA DÒNG NÀY -->
            <a class="navbar-brand" href="${pageContext. request.contextPath}/">🧬 Xếp Lịch GA</a>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <% if (result != null && best != null) { %>
                    <% if (best.isValid()) { %>
                        <div class="alert alert-success">
                            <h4>✅ Thành Công!</h4>
                            <p>Đã tạo lịch hợp lệ cho <%= semester %> <%= academicYear %></p>
                        </div>
                    <% } else { %>
                        <div class="alert alert-warning">
                            <h4>⚠️ Có Vi Phạm</h4>
                            <p>Lịch có <%= best.getHardConstraintViolations() %> vi phạm nghiêm trọng</p>
                        </div>
                    <% } %>

                    <div class="card">
                        <div class="card-header bg-primary text-white">
                            <h5>📊 Thống Kê</h5>
                        </div>
                        <div class="card-body">
                            <table class="table">
                                <tr>
                                    <td><strong>Số thế hệ: </strong></td>
                                    <td><%= result.getGenerationsExecuted() %></td>
                                </tr>
                                <tr>
                                    <td><strong>Thời gian:</strong></td>
                                    <td><%= result.getExecutionTimeSeconds() %> giây</td>
                                </tr>
                                <tr>
                                    <td><strong>Fitness tốt nhất:</strong></td>
                                    <td><%= String.format("%.2f", best.getFitnessScore()) %></td>
                                </tr>
                                <tr>
                                    <td><strong>Hard violations:</strong></td>
                                    <td><%= best.getHardConstraintViolations() %></td>
                                </tr>
                                <tr>
                                    <td><strong>Soft violations:</strong></td>
                                    <td><%= best.getSoftConstraintViolations() %></td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="mt-3">
                        <!-- ✅ SỬA CÁC DÒNG NÀY -->
                        <a href="${pageContext.request. contextPath}/schedule?action=list&semester=<%= semester %>&academicYear=<%= academicYear %>" 
                           class="btn btn-success">
                            📅 Xem Lịch Vừa Tạo
                        </a>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">🏠 Trang Chủ</a>
                    </div>

                <% } else { %>
                    <div class="alert alert-danger">
                        <h4>❌ Lỗi</h4>
                        <p>Không thể tạo lịch.  Vui lòng thử lại!</p>
                        <p><small>Result: <%= result %>, Best: <%= best %></small></p>
                    </div>
                    <div class="mt-3">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">🏠 Trang Chủ</a>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>