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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<String, Object>() {
        }));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Object> pageVariables = new HashMap<>();
            String senderName = req.getParameter("senderName");
            String senderPass = req.getParameter("senderPass");
            Long count = Long.valueOf(req.getParameter("count"));
            String nameTo = req.getParameter("nameTo");

            BankClientService bankClientService = new BankClientService();
            BankClient bankClientServiceSender = bankClientService.getClientByName(senderName);
            if (bankClientServiceSender.getPassword().equals(senderPass)) {
                if (bankClientServiceSender == null | bankClientService.getClientByName(nameTo) == null) {
                    // тправитель не зарегистрирован
                    pageVariables.put("message", "transaction rejected");
                    resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                } else {
                    if (bankClientService.sendMoneyToClient(bankClientServiceSender, nameTo, count)) {
                        // денег хватает
                        pageVariables.put("message", "The transaction was successful");
                        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                    } else {
                        // денег не хватает
                        pageVariables.put("message", "transaction rejected");
                        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                    }
                }
            } else {
                pageVariables.put("message", "transaction rejected");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

