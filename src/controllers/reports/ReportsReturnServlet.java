package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsReturnServlet
 */
@WebServlet("/reports/return")
public class ReportsReturnServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsReturnServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {

            EntityManager em = DBUtil.createEntityManager();
            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
            Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

            if(request.getParameter("approval_status").equals(String.valueOf(r.getApproval_status()))){

            r.setApproval_status(r.getApproval_status() + 1);
            r.setDenial_text(request.getParameter("denial_text"));
            r.setRepudiation_user(login_employee);

            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "日報を否認しました");
            response.sendRedirect(request.getContextPath() + "/reports/index");

            }else{
                em.close();
                request.getSession().setAttribute("flush", "更新に失敗しました");
                response.sendRedirect(request.getContextPath() + "/reports/index");
            }

        }
    }

}
