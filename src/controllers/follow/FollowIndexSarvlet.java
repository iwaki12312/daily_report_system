package controllers.follow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        HttpSession session = request.getSession();


        String name = (String)session.getAttribute("name");

        if(name == null){
            name = "";
        }

        if(request.getParameter("search") != null){
            name = (String)request.getParameter("search");
            session.setAttribute("name", name);
        }


        //  検索文字列を含むリストを取得
        List<Employee> employees = em.createNamedQuery("getFollowEmployeesIndex", Employee.class)
                .setParameter("employee_id", login_employee.getId())
                .setParameter("name", "%" + name + "%")
                .getResultList();


        String followsearch = (String)session.getAttribute("followsearch");
        if(followsearch == null){
            followsearch = "off";
        }

        String followersearch = (String)session.getAttribute("followersearch");
        if(followersearch == null){
            followersearch = "off";
        }

        if(request.getParameter("followsearch") != null){
            followsearch = (String)request.getParameter("followsearch");
            session.setAttribute("followsearch", followsearch);
        }

        if(request.getParameter("followersearch") != null){
            followersearch = (String)request.getParameter("followersearch");
            session.setAttribute("followersearch", followersearch);
        }

        if(followsearch.equals("on") && followersearch.equals("on")){  // 相互フォロー以外のユーザーを除外

                Iterator<Employee> it = employees.iterator();
                while (it.hasNext()) {

                Employee employee = it.next();

                Follow follow = null;
                Follow follower = null;

                try{
                    follow = em.createNamedQuery("getFollows", Follow.class)
                            .setParameter("employee_id", login_employee.getId())
                            .setParameter("follow", employee.getId())
                            .getSingleResult();
                }catch (Exception e) {
                    e.printStackTrace();
                }

                try{
                    follower = em.createNamedQuery("getFollowers", Follow.class)
                            .setParameter("login_employee_id", login_employee.getId())
                            .setParameter("employee_id", employee.getId())
                            .getSingleResult();
                }catch (Exception e) {
                    e.printStackTrace();
                }

                if(follow == null || follower == null)it.remove();
            }
        }else if(followsearch.equals("on")){    // フォローしている従業員以外を除外
            Iterator<Employee> it = employees.iterator();
            while (it.hasNext()) {

            Employee employee = it.next();

            Follow follow = null;

            try{
                follow = em.createNamedQuery("getFollows", Follow.class)
                        .setParameter("employee_id", login_employee.getId())
                        .setParameter("follow", employee.getId())
                        .getSingleResult();
            }catch (Exception e) {
                e.printStackTrace();
            }

            if(follow == null)it.remove();

            }
        }else if(followersearch.equals("on")){    // フォローされている従業員以外を除外
            Iterator<Employee> it = employees.iterator();
            while (it.hasNext()) {

            Employee employee = it.next();

            Follow follower = null;

            try{
                follower = em.createNamedQuery("getFollowers", Follow.class)
                        .setParameter("login_employee_id", login_employee.getId())
                        .setParameter("employee_id", employee.getId())
                        .getSingleResult();
            }catch (Exception e) {
                e.printStackTrace();
            }

            if(follower == null)it.remove();

            }

        }


        int page = 1;

        if(request.getParameter("followsearch") != null){  // 検索ボタンが押された場合(followsearchパラメータは必ず入る為)
            session.removeAttribute("page");
        }

        if(session.getAttribute("page") != null){
            page = (Integer)session.getAttribute("page");

        }

        try{
            page = Integer.parseInt(request.getParameter("page"));
            session.setAttribute("page", page);
        } catch(NumberFormatException e) { }


        int employees_count = employees.size();



        // ページネーション用に15件ずつ切り取り
        List<Employee> displayEmployees = new ArrayList<Employee>();

        employees.subList(0, (page - 1) * 15).clear();

        Iterator <Employee> it = employees.iterator();
        int i = 0;
        while (it.hasNext() && i < 15) {
            displayEmployees.add(it.next());

            i++;

        }


        int followCheck[] = new int[displayEmployees.size()];
        int j = 0;
        for(Employee employee : displayEmployees){
            Follow follow = null;

            try{
                follow = em.createNamedQuery("getFollows", Follow.class)
                        .setParameter("employee_id", login_employee.getId())
                        .setParameter("follow", employee.getId())
                        .getSingleResult();
            }catch (Exception e) {
                e.printStackTrace();
            }

            if(follow == null){
                followCheck[j] = 0;
            }else{
                followCheck[j] = 1;
            }

            j++;
        }


        em.close();

        request.setAttribute("followCheck", followCheck);
        request.setAttribute("employees", displayEmployees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("name", name);
        request.setAttribute("followsearch", followsearch);
        request.setAttribute("followersearch", followersearch);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/index.jsp");
        rd.forward(request, response);
    }

}
