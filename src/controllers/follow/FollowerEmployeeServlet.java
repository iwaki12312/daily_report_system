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
 * Servlet implementation class FollowEmployeeServlet
 */
@WebServlet("/follow/followeremployee")
public class FollowerEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowerEmployeeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        List<Follow> followerEmployeesDeta = em.createNamedQuery("getFollowerEmployees", Follow.class)
                                                        .setParameter("login_employee_id", login_employee.getId())
                                                        .getResultList();

        List<Employee> followerEmployees = new ArrayList<Employee>();
        int followCheck[] = new int[followerEmployeesDeta.size()];

        int i = 0;

        for(Follow followerEmployeesId : followerEmployeesDeta){
            followerEmployees.add(em.find(Employee.class, followerEmployeesId.getEmployee_id()));

            Follow follow = null;

            try{
                follow = em.createNamedQuery("getFollows", Follow.class)
                        .setParameter("employee_id", login_employee.getId())
                        .setParameter("follow", followerEmployeesId.getEmployee_id())
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


        Long employees_count = (long)em.createNamedQuery("getFollowerCount", Long.class)
                                                        .setParameter("employee_id", login_employee.getId())
                                                        .getSingleResult();


        em.close();


        request.setAttribute("followCheck", followCheck);
        request.setAttribute("followerEmployees", followerEmployees);
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("employees_count", employees_count);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/followerEmployee.jsp");
        rd.forward(request, response);




    }
}
