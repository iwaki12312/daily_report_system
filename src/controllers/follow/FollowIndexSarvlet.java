package controllers.follow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowIndexSarvlet
 */
@WebServlet("/follow/index")
public class FollowIndexSarvlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowIndexSarvlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        // 名前検索用の文字列を取得
        String nameStr = request.getParameter("search");
        String nameFilter = null;
        if(nameStr == null || nameStr.equals("")){
            nameFilter = " 1 = 1 "; // 検索文字列が空の場合は全てtrueになる条件をクエリに挿入
        }else{
            nameFilter = " e.name LIKE '%" + nameStr + "%'"; // 検索文字列が存在する場合文字列を含むレコードを検索
        }

        // フォローしてる従業員検索用のパラメータ取得
        String followStr = request.getParameter("followsearch");

        // フォローされている従業員検索用のパラメータ取得
        String followerStr = request.getParameter("followersearch");

        //検索するテーブルを結合したクエリ
        String searchTable = " FROM Employee e LEFT OUTER JOIN Follow f ON e.id = f.employee.id LEFT OUTER JOIN Follow j ON f.follow = j.employee WHERE ";

        //ページネーション用パラメータ
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }


        List<Employee> employees = new ArrayList<Employee>();
        long employees_count = 0;

        //フォローステータスによる絞り込みが無い場合の検索
        if((followStr == null || followStr.equals("") || followStr.equals("off"))
                && (followerStr == null || followerStr.equals("") || followerStr.equals("off"))){

            employees = em.createQuery("SELECT distinct e " + searchTable + nameFilter + " ORDER BY e.id DESC", Employee.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            employees_count = (long)em.createQuery("SELECT COUNT(DISTINCT e) " + searchTable + nameFilter, Long.class)
                    .getSingleResult();
        }else if(followStr.equals("on") && followerStr.equals("off")){ //フォローしてるユーザーを取得

            employees =  em.createQuery("SELECT distinct f.follow " + searchTable + nameFilter
                    + " AND f.employee = :login_employee ORDER BY f.follow.id DESC",Employee.class)
                    .setParameter("login_employee", login_employee)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            employees_count = (long)em.createQuery("SELECT COUNT(DISTINCT f.follow) " + searchTable + nameFilter
                    + " AND f.employee = :login_employee" , Long.class)
                    .setParameter("login_employee", login_employee)
                    .getSingleResult();


        }else if(followStr.equals("off") && followerStr.equals("on")){ //フォローされているユーザーを取得

            employees =  em.createQuery("SELECT distinct e " + searchTable + nameFilter
                    + " AND f.follow = :login_employee ORDER BY e.id DESC",Employee.class)
                    .setParameter("login_employee", login_employee)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            employees_count = (long)em.createQuery("SELECT COUNT(DISTINCT e) " + searchTable + nameFilter
                    + " AND f.follow = :login_employee" , Long.class)
                    .setParameter("login_employee", login_employee)
                    .getSingleResult();


        }else if(followStr.equals("on") && followerStr.equals("on")){ //相互フォローのユーザーを取得

            employees =  em.createQuery("SELECT distinct e " + searchTable + nameFilter
                    + " AND j.follow = f.employee AND f.follow = :login_employee ORDER BY e.id DESC",Employee.class)
                    .setParameter("login_employee", login_employee)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            employees_count = (long)em.createQuery("SELECT COUNT(DISTINCT e) " + searchTable + nameFilter
                    + " AND j.follow = f.employee AND f.follow = :login_employee" , Long.class)
                    .setParameter("login_employee", login_employee)
                    .getSingleResult();

        }

        int followCheck[] = new int[employees.size()];
        int i = 0;
        for(Employee employee : employees){
            Follow follow = null;

            try{
                follow = em.createNamedQuery("getFollows", Follow.class)
                        .setParameter("employee", login_employee)
                        .setParameter("follow", employee)
                        .getSingleResult();
            }catch (Exception e) {
                e.printStackTrace();
            }

            if(follow == null){
                followCheck[i] = 0;
            }else{
                followCheck[i] = 1;
            }

            i++;
        }



        em.close();

        request.setAttribute("employees_count", employees_count);
        request.setAttribute("employees", employees);
        request.setAttribute("page", page);
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("name", nameStr);
        request.setAttribute("followsearch", followStr);
        request.setAttribute("followersearch", followerStr);
        request.setAttribute("followCheck", followCheck);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/index.jsp");
        rd.forward(request, response);
    }

}
