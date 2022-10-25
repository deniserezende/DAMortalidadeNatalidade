package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CargaController",
            urlPatterns = {
                "",
                "/carga"
            }
)

public class CargaController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "": {
                dispatcher = request.getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/carga": {
                session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/view/index.jsp");
            }
        }
    }
}
