package controllers.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import models.Follow;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report  r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        Employee employee = r.getEmployee();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



        Attendance a = null;
        try{a = em.createNamedQuery("getTimes", Attendance.class)
                                            .setParameter("employee", r.getEmployee())
                                            .setParameter("date", dateFormat.format(r.getReport_date()))
                                            .getSingleResult();
        }catch (Exception e) {
           e.printStackTrace();
        }

        Follow b = null;

        try{
            b = em.createNamedQuery("getFollows", Follow.class)
            .setParameter("employee_id", login_employee.getId())
            .setParameter("follow", employee.getId())
            .getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }


        int followCheck_1 = employee.getId();
        int followCheck_2 = login_employee.getId();

        if(followCheck_1 == followCheck_2){
            request.setAttribute("followDeta", 2);
        }else if(b == null){
            request.setAttribute("followDeta", 0);
        }else if(b != null){
            request.setAttribute("followDeta", 1);
        }

        //いいねをしているかチェック

        Like like = null;

        try{
            like = em.createNamedQuery("getLikeCheck", Like.class)
                    .setParameter("employee", login_employee)
                    .setParameter("report", r)
                    .getSingleResult();
        }catch (Exception e) {
           e.printStackTrace();
        }

        //表示するレポートのいいね数を取得
        long likeCount = em.createNamedQuery("getLikeCount", Long.class)
                .setParameter("report", r)
                .getSingleResult();



        em.close();

        request.setAttribute("employee", employee);
        request.setAttribute("report", r);
        request.setAttribute("likeCount", likeCount);
        request.setAttribute("attendance", a);
        request.setAttribute("like", like);
        request.setAttribute("_token", request.getSession().getId());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}