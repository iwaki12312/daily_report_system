package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Client;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateServlet
 */
@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateServlet() {
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

            Report r = em.find(Report.class, (Integer)(request.getSession().getAttribute("report_id")));

            if(request.getParameter("approval_status").equals(String.valueOf(r.getApproval_status()))){

                r.setReport_date(Date.valueOf(request.getParameter("report_date")));
                r.setTitle(request.getParameter("title"));
                r.setContent(request.getParameter("content"));
                r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

                //取引先が選択されている場合は取引先と商談状況を登録
                String clientId = request.getParameter("client");
                if(!clientId.equals("")){
                    r.setClient(em.find(Client.class , Integer.parseInt(clientId)));
                    r.setNegotiation_status(request.getParameter("negotiation_status"));
                }else{
                    r.setClient(null);
                    r.setNegotiation_status(null);
                }

                //提出ボタンが押された場合は承認ステータスに１を足して承認待ちに変更
                String cmd = request.getParameter("cmd");
                if(cmd.equals("提出する")){
                    if(r.getSection_manager_approval() == null){
                        r.setApproval_status(1);
                    }else{
                        r.setApproval_status(3);
                    }
                //取り下げるボタンが押された場合は承認ステータスを編集中に変更
                }else if(cmd.equals("取り下げる")){
                    r.setApproval_status(0);
                //再提出するボタンが押された場合は承認ステータスから1を引いて承認待ちにステータスを変更
                }else if(cmd.equals("再提出する")){
                    r.setApproval_status(r.getApproval_status() - 1);
                }



                List<String> errors = ReportValidator.validate(r);
                if(errors.size() > 0) {
                    em.close();

                    request.setAttribute("_token", request.getSession().getId());
                    request.setAttribute("report", r);
                    request.setAttribute("errors", errors);

                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
                    rd.forward(request, response);
                } else {
                    em.getTransaction().begin();
                    em.getTransaction().commit();
                    em.close();
                    request.getSession().setAttribute("flush", "更新が完了しました。");

                    request.getSession().removeAttribute("report_id");

                    response.sendRedirect(request.getContextPath() + "/reports/index");
                }
            }else{
                em.close();
                request.getSession().setAttribute("flush", "更新に失敗しました");
                response.sendRedirect(request.getContextPath() + "/reports/index");
            }
        }
    }

}