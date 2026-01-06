package controller;

import service.GAService;
import algorithm.GeneticAlgorithm.GAResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/run-ga")
public class RunGAServlet extends HttpServlet {

    private GAService gaService = new GAService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form ban đầu (chưa có kết quả)
        request.getRequestDispatcher("/views/run-ga.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Nhận POST /run-ga lúc " + new java.util.Date());

        String semester = request.getParameter("semester");
        String academicYear = request.getParameter("academicYear");
        if (semester == null || semester.isEmpty() || academicYear == null || academicYear.isEmpty()) {
            request.setAttribute("error", "Chưa chọn học kỳ hoặc năm học!");
            request.getRequestDispatcher("/views/run-ga.jsp").forward(request, response);
            return;
        }

        GAResult result = gaService.runGA(semester, academicYear);
        if (result == null) {
            request.setAttribute("error", "Không thể chạy GA! Kiểm tra dữ liệu đầu vào hoặc lỗi thuật toán.");
            request.getRequestDispatcher("/views/run-ga.jsp").forward(request, response);
            return;
        }

        // Thống kê cho view
        request.setAttribute("generations", result.getGenerationsExecuted());
        request.setAttribute("fitness", result.getBestFitness());
        request.setAttribute("hardViolations", result.getBestHardViolations());
        request.setAttribute("softViolations", result.getBestSoftViolations());
        request.setAttribute("scheduleCount", result.getScheduleCount());
        request.setAttribute("semester", semester);
        request.setAttribute("academicYear", academicYear);

        // Không còn nút "Chạy tiếp" – chạy tự động đến khi dừng
        request.getRequestDispatcher("/views/run-ga.jsp").forward(request, response);
    }
}