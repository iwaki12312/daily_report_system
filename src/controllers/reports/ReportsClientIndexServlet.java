package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Client;
import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsClientIndexServlet
 */
@WebServlet("/reports/clientindex")
public class ReportsClientIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsClientIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        EntityManager em = DBUtil.createEntityManager();

        if(login_employee.getPosition_flag() > 1){
            Client client = em.find(Client.class, Integer.parseInt(request.getParameter("id")));

            int page;
            try{
                page = Integer.parseInt(request.getParameter("page"));
            } catch(Exception e) {
                page = 1;
            }

            List<Report>reports = em.createNamedQuery("getClientReports", Report.class)
                        .setParameter("client", client)
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();

            Long reports_count = (long)em.createNamedQuery("getClientReportsCount", Long.class)
                        .setParameter("client", client)
                        .getSingleResult();


            em.close();

            // レポート承認時リダイレクト用フラグ
            request.getSession().setAttribute("approvalRedirectFlag" , "clientIndex");

            request.setAttribute("reports", reports);
            request.setAttribute("reports_count", reports_count);
            request.setAttribute("page", page);
            request.setAttribute("client", client);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/clientindex.jsp");
            rd.forward(request, response);
        }else{
            em.close();
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

}
