<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<! DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hệ thống Xếp Lịch Giảng Dạy - GA</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container">
			<a class="navbar-brand" href="<%= contextPath %>/">🧬 Xếp Lịch GA</a>
		</div>
	</nav>

	<div class="container mt-5">
		<div class="row">
			<div class="col-md-12 text-center">
				<h1>🎓 Hệ Thống Xếp Lịch Giảng Dạy</h1>
				<p class="lead">Sử dụng Thuật Toán Di Truyền (Genetic Algorithm)</p>
			</div>
		</div>

		<div class="row mt-5">
			<div class="col-md-4">
				<div class="card">
					<div class="card-body text-center">
						<h5 class="card-title">🚀 Chạy Thuật Toán</h5>
						<p class="card-text">Tạo lịch mới cho học kỳ</p>
						<a href="/CourseRegisterSystem/views/run-ga.jsp"
							class="btn btn-primary">Bắt Đầu</a>
					</div>
				</div>
			</div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body text-center">
						<h5 class="card-title">📅 Xem Lịch</h5>
						<p class="card-text">Xem lịch đã tạo</p>
						<a
							href="<%= contextPath %>/schedule? action=list&semester=HK1&academicYear=2025-2026"
							class="btn btn-success">Xem Lịch</a>
					</div>
				</div>
			</div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body text-center">
						<h5 class="card-title">📊 Lịch Tuần</h5>
						<p class="card-text">Xem lịch dạng bảng</p>
						<a
							href="<%= contextPath %>/schedule?action=weekly&semester=HK1&academicYear=2025-2026"
							class="btn btn-info">Xem Bảng</a>
					</div>
				</div>
			</div>

			<!-- ✅ NÚT MỚI -->
			<div class="col-md-3">
				<div class="card">
					<div class="card-body text-center">
						<h5 class="card-title">👨‍🏫 Lịch Giáo Viên</h5>
						<p class="card-text">Xem lịch từng GV</p>
						<a
							href="<%= contextPath %>/teacher-schedule? semester=HK1&academicYear=2025-2026"
							class="btn btn-warning">Xem Lịch GV</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle. min.js"></script>
</body>
</html>