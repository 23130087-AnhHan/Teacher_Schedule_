package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet. annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet. http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java. io.IOException;
//ược thiết kế để xử lý các yêu cầu HTTP liên quan đến trang homepage của ứng dụng.
//Nó đóng vai trò như một cổng vào chính (default endpoint) khi người dùng truy cập ứng dụng thông qua URL root
@WebServlet("/")
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to index.jsp
        request.getRequestDispatcher("/views/index.jsp").forward(request, response);
    }
}