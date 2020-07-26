package controllers.follow;

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
 * Servlet implementation class FollowEmployeeReportSevlet
 */
@WebServlet("/follow/reportindex")
public class FollowEmployeeReportSevlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowEmployeeReportSevlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Employee employee = em.find(Employee.class, Integer.parseInt(request.getParameter("id"))) ;

        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                .setParameter("employee",employee )
                .getResultList();

        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                .setParameter("employee", employee)
                .getSingleResult();

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        request.setAttribute("reports", reports);
        request.setAttribute("employee", employee);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/followReportIndex.jsp");
        rd.forward(request, response);


    }

}
