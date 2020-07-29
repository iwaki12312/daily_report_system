package controllers.toppage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM");
        Attendance b = null;

        try{b = em.createNamedQuery("getTimes", Attendance.class)
                .setParameter("employee", login_employee)
                .setParameter("date", dateFormat.format(date))
                .getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(b == null || (b.getInTime() == null && b.getOutTime() == null)){
            request.setAttribute("attendance", 0);
        }else if(b.getInTime() != null && b.getOutTime() == null){
            request.setAttribute("attendance", 1);
        }else if(b.getInTime() != null && b.getOutTime() != null){
            request.setAttribute("attendance", 2);
        }



        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                                  .setParameter("employee", login_employee)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();

        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                                     .setParameter("employee", login_employee)
                                     .getSingleResult();

        // 課長未承認のレポート取得
        List<Report> notSectionManagerApprovalreports = em.createNamedQuery("getNotSectionManagerApprovalReports", Report.class)
                .setParameter("login_employee", login_employee)
                .getResultList();
        int sectionManagerCount = notSectionManagerApprovalreports.size();

        // 部長未承認のレポート取得
        List<Report> notManagerApprovalreports = em.createNamedQuery("getNotManagerApprovalReports", Report.class)
                .setParameter("login_employee", login_employee)
                .getResultList();
        int managerCount = notManagerApprovalreports.size();


        em.close();

        request.setAttribute("reports", reports);
        request.setAttribute("sectionManagerCount", sectionManagerCount);
        request.setAttribute("managerCount", managerCount);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        request.setAttribute("employee", login_employee);
        request.setAttribute("year", dateFormatYear.format(date));
        request.setAttribute("month", dateFormatMonth.format(date));

        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
