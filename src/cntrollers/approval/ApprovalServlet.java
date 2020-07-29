package cntrollers.approval;

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
 * Servlet implementation class ApprovalServlet
 */
@WebServlet("/approval")
public class ApprovalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApprovalServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int reportId = Integer.parseInt(request.getParameter("reportId"));

        Report r = em.find(Report.class, reportId);


        // ログインユーザーが課長の場合は課長承認する
        if(login_employee.getPosition_flag().equals(2)){
            r.setSection_manager_approval(login_employee);
        }

        // ログインユーザーが部長の場合は部長承認する
        if(login_employee.getPosition_flag().equals(3)){
            r.setManager_approval(login_employee);
        }


        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();


        String approvalRedirectFlag = (String)request.getSession().getAttribute("approvalRedirectFlag");

        request.getSession().removeAttribute("approvalRedirectFlag");
        request.getSession().setAttribute("flush", "日報を承認しました");


        // 遷移元へリダイレクトする
        if(approvalRedirectFlag.equals("reportIndex")){
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }else if(approvalRedirectFlag.equals("approvalIndex")){
            response.sendRedirect(request.getContextPath() + "/approval/reports?position=" + login_employee.getPosition_flag());
        }

    }

}
