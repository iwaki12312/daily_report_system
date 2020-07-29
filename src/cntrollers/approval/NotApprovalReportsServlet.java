package cntrollers.approval;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class NotApprovalReportsServlet
 */
@WebServlet("/approval/reports")
public class NotApprovalReportsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotApprovalReportsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        List<Report> reports = null;


        // ユーザーが課長だった場合課長承認待ちのレポートを取得
        if(Integer.parseInt(request.getParameter("position")) == 2){
           reports = em.createNamedQuery("getNotSectionManagerApprovalReports", Report.class)
                    .setParameter("login_employee", login_employee)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();
        }

        // ユーザーが部長だった場合部長承認待ちのレポートを取得
        if(Integer.parseInt(request.getParameter("position")) == 3){
            reports = em.createNamedQuery("getNotManagerApprovalReports", Report.class)
                     .setParameter("login_employee", login_employee)
                     .setFirstResult(15 * (page - 1))
                     .setMaxResults(15)
                     .getResultList();
         }



        em.close();

        // レポート承認時リダイレクト用フラグ
        request.getSession().setAttribute("approvalRedirectFlag" , "approvalIndex");

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports.size());
        request.setAttribute("page", page);
        request.setAttribute("login_employee", login_employee);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/notapprovalreports.jsp");
        rd.forward(request, response);

    }

}
