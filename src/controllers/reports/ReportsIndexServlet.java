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
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
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
        long reports_count = 0;

        // 絞り込み検索用のパラメータ取得
        String filter = request.getParameter("filter");
        String clientFilter = request.getParameter("clientFilter");
        String clientFilterStr = null;
        Client client = null;
        String likeFilter = request.getParameter("likeFilter");;
        String likeFilterStr = null;

        //取引先による絞り込みがある場合のフィルター
        if(clientFilter == null || clientFilter.equals("")){
            clientFilterStr = "1 = 1"; //全て表示の場合は全てtrueになる条件を挿入
        }else{
            client = em.find(Client.class, Integer.parseInt(clientFilter));
            clientFilterStr = "r.client.id = " + client.getId();
        }

        //いいねによる絞り込みがある場合のフィルター
        if(likeFilter == null || likeFilter.equals("")){
            likeFilterStr = " AND 1 = 1"; //全て表示の場合は全てtrueになる条件を挿入
        }else{
            likeFilterStr = " AND l.employee.id =" + login_employee.getId();
        }

        //検索用テーブルの結合クエリ
        String query = "SELECT distinct r FROM Report AS r LEFT OUTER JOIN Like AS l ON r.id = l.report.id WHERE ";
        String queryCount = "SELECT COUNT(DISTINCT r) FROM Report AS r LEFT OUTER JOIN Like AS l ON r.id = l.report.id WHERE ";


        //以下5つの条件付き検索機能全てに取引先による絞り込みを追加済
        //以下5つの条件付き検索機能全てにいいねのステータスによる絞り込みを追加済

        //絞り込み検索のパラメータが存在しないか空文字の場合は全レポートを取得
        if(filter == null ||filter.equals("")){
            reports = em.createQuery(query +  clientFilterStr + likeFilterStr + " ORDER BY r.id DESC", Report.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            reports_count = (long)em.createQuery(queryCount +  clientFilterStr + likeFilterStr, Long.class)
                       .getSingleResult();

        //絞り込み検索のパラメータが課長承認待ちの場合該当レポートを取得
        }else if(filter.equals("sectionManager")){
            reports = em.createQuery(query + clientFilterStr + likeFilterStr + " AND r.section_manager_approval = null ORDER BY r.id DESC", Report.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            reports_count = (long)em.createQuery(queryCount + clientFilterStr + likeFilterStr + " AND r.section_manager_approval = null", Long.class)
                       .getSingleResult();

        //絞り込み検索のパラメータが部長承認待ちの場合該当レポートを取得
        }else if(filter.equals("manager")){
            reports = em.createQuery(query + clientFilterStr + likeFilterStr + " AND r.manager_approval = null AND r.section_manager_approval <> null ORDER BY r.id DESC", Report.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            reports_count = (long)em.createQuery(queryCount + clientFilterStr + likeFilterStr + " AND r.manager_approval = null AND r.section_manager_approval <> null", Long.class)
                       .getSingleResult();

        //絞り込み検索のパラメータが承認済みの場合該当レポートを取得
        }else if(filter.equals("approved")){
            reports = em.createQuery(query + clientFilterStr + likeFilterStr + " AND r.manager_approval <> null AND r.section_manager_approval <> null ORDER BY r.id DESC", Report.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            reports_count = (long)em.createQuery(queryCount + clientFilterStr + likeFilterStr + " AND r.manager_approval <> null AND r.section_manager_approval <> null", Long.class)
                       .getSingleResult();

        //絞り込み検索のパラメータが自分が承認した日報の場合該当レポートを取得
        }else if(filter.equals("myApproved")){
            reports = em.createQuery(query + clientFilterStr + likeFilterStr + " AND (r.manager_approval.id =" + login_employee.getId() + " OR r.section_manager_approval.id =" + login_employee.getId() + ") ORDER BY r.id DESC", Report.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            reports_count = (long)em.createQuery(queryCount + clientFilterStr + likeFilterStr + " AND (r.manager_approval.id =" + login_employee.getId() + " OR r.section_manager_approval.id =" + login_employee.getId() + ")", Long.class)
                       .getSingleResult();
        }

        // 絞り込み検索用に全ての取引先を取得
        List<Client> clients = em.createNamedQuery("getAllClients", Client.class)
                .getResultList();


        em.close();

        // レポート承認時リダイレクト用フラグ
        request.getSession().setAttribute("approvalRedirectFlag" , "reportIndex");


        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("clients", clients);
        request.setAttribute("page", page);
        request.setAttribute("filter", filter);
        request.setAttribute("clientFilter", clientFilter);
        request.setAttribute("likeFilter", likeFilter);
        request.setAttribute("login_employee", login_employee);


        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

}
