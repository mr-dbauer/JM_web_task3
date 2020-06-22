package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<String, Object>() {
        }));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.valueOf(req.getParameter("money"));
        BankClientService bankClientService = new BankClientService();
        BankClient bankClient = new BankClient(name, password, money);
        try {
            if (bankClientService.addClient(bankClient)) {
                pageVariables.put("message", "Add client successful");
                //resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
            } else {
                pageVariables.put("message", "Client not add");
                //resp.setStatus(HttpServletResponse.SC_FOUND);
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
            }
        } catch (DBException e) {
            e.printStackTrace();
        }

    }
}
