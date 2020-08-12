package controllers.approval;

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

        if(request.getParameter("approval_status").equals(String.valueOf(r.getApproval_status()))){


            request.getSession().setAttribute("flush", "日報を承認しました");

            // ログインユーザーが課長の場合は課長承認する
            if(login_employee.getPosition_flag().equals(2) && r.getSection_manager_approval() == null){
                r.setSection_manager_approval(login_employee);

            //他ユーザーが承認済みの場合は承認不可
            }else if(login_employee.getPosition_flag().equals(2) && r.getSection_manager_approval() != null){
                request.getSession().setAttribute("flush", "既に承認済みの日報です");
            }

            // ログインユーザーが部長の場合は部長承認する
            if(login_employee.getPosition_flag().equals(3) && r.getManager_approval() == null){
                r.setManager_approval(login_employee);

            //他ユーザーが承認済みの場合は承認不可
            }else if(login_employee.getPosition_flag().equals(3) && r.getManager_approval() != null){
                request.getSession().setAttribute("flush", "既に承認済みの日報です");
            }

            //承認ステータスに2を足してステータス変更、否認ユーザーと否認理由を破棄
            r.setApproval_status(r.getApproval_status() + 2);
            r.setDenial_text(null);
            r.setRepudiation_user(null);


            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

        }else{
            em.close();
            request.getSession().setAttribute("flush", "更新に失敗しました");
        }

        String approvalRedirectFlag = (String)request.getSession().getAttribute("approvalRedirectFlag");
        request.getSession().removeAttribute("approvalRedirectFlag");

        // 遷移元へリダイレクトする
        if(approvalRedirectFlag.equals("reportIndex") || approvalRedirectFlag == null){
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }else if(approvalRedirectFlag.equals("approvalIndex")){
            response.sendRedirect(request.getContextPath() + "/approval/reports?position=" + login_employee.getPosition_flag());
        }else if(approvalRedirectFlag.equals("clientIndex")){
            response.sendRedirect(request.getContextPath() + "/reports/clientindex?id=" + r.getClient().getId());
        }

    }

}
