<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String contextPath = request. getContextPath();
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

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-6 offset-md-3">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4>🚀 Chạy Genetic Algorithm</h4>
                    </div>
                    <div class="card-body">
                        <form method="POST" action="<%= contextPath %>/run-ga">
                            <div class="mb-3">
                                <label class="form-label">Học kỳ</label>
                                <select name="semester" class="form-select" required>
                                    <option value="HK1" selected>Học kỳ 1</option>
                                    <option value="HK2">Học kỳ 2</option>
                                    <option value="HK3">Học kỳ 3 (Hè)</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Năm học</label>
                                <input type="text" name="academicYear" class="form-control" 
                                       value="2025-2026" required>
                            </div>

                            <div class="alert alert-info">
                                <strong>📝 Lưu ý:</strong> Thuật toán sẽ xóa lịch cũ và tạo lịch mới! 
                            </div>

                            <button type="submit" class="btn btn-primary w-100">
                                🚀 Chạy Thuật Toán
                            </button>
                        </form>
                    </div>
                </div>
                
                <div class="mt-3">
                    <a href="<%= contextPath %>/" class="btn btn-secondary">
                        ← Quay lại
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>