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


        // 承認ステータスによる検索用のパラメータ取得、対応する検索条件のクエリを代入
        String filterStr = request.getParameter("filter");
        String filter = null;
        if(filterStr == null || filterStr.equals("")){
            filter = "1 = 1"; //全て表示の場合は全てtrueになる条件を挿入
        }else if(filterStr.equals("sectionManager")){
            filter = "r.section_manager_approval = null";
        }else if(filterStr.equals("manager")){
            filter = "r.manager_approval = null AND r.section_manager_approval <> null";
        }else if(filterStr.equals("approved")){
            filter = "r.manager_approval <> null AND r.section_manager_approval <> null";
        }else if(filterStr.equals("myApproved")){
            filter = "(r.manager_approval.id =" + login_employee.getId() + " OR r.section_manager_approval.id =" + login_employee.getId() + ")";
        }


        //取引先による検索用のパラメータ取得、対応する検索条件のクエリを代入
        String clientFilterStr = request.getParameter("clientFilter");
        String clientFilter = null;
        Client client = null;

        if(clientFilterStr == null || clientFilterStr.equals("")){
            clientFilter = " AND 1 = 1"; //全て表示の場合は全てtrueになる条件を挿入
        }else{
            client = em.find(Client.class, Integer.parseInt(clientFilterStr));
            clientFilter = " AND r.client.id = " + client.getId();
        }

        //いいねによる検索用のパラメータ取得、対応する検索条件のクエリを代入
        String likeFilterStr = request.getParameter("likeFilter");;
        String likeFilter = null;

        if(likeFilterStr == null || likeFilterStr.equals("")){
            likeFilter = " AND 1 = 1"; //全て表示の場合は全てtrueになる条件を挿入
        }else{
            likeFilter = " AND l.employee.id =" + login_employee.getId();
        }

        //検索するテーブルを結合したクエリ
        String query = "SELECT distinct r FROM Report AS r LEFT OUTER JOIN Like AS l ON r.id = l.report.id WHERE ";
        String queryCount = "SELECT COUNT(DISTINCT r) FROM Report AS r LEFT OUTER JOIN Like AS l ON r.id = l.report.id WHERE ";

        //検索
        List<Report> reports = em.createQuery(query + filter + clientFilter + likeFilter + " ORDER BY r.id DESC", Report.class)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        Long reports_count = (long)em.createQuery(queryCount + filter +  clientFilter + likeFilter, Long.class)
                   .getSingleResult();

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
        request.setAttribute("filter", filterStr);
        request.setAttribute("clientFilter", clientFilterStr);
        request.setAttribute("likeFilter", likeFilterStr);
        request.setAttribute("login_employee", login_employee);


        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

}
