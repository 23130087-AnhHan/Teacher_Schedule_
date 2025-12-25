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
    
    @Override  // ✅ THÊM DÒNG NÀY
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Show form
        request. getRequestDispatcher("/views/run-ga. jsp").forward(request, response);
    }
    
    @Override  // ✅ THÊM DÒNG NÀY
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get parameters
        String semester = request.getParameter("semester");
        String academicYear = request.getParameter("academicYear");
        
        // Run GA
        GAResult result = gaService.runGA(semester, academicYear);
        
        // Set result to request
        request. setAttribute("result", result);
        request.setAttribute("semester", semester);
        request.setAttribute("academicYear", academicYear);
        
        // Forward to result page
        request.getRequestDispatcher("/views/ga-result.jsp").forward(request, response);
    }
}