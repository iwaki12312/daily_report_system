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
@WebServlet("/follow/followemployee")
public class FollowEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowEmployeeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        List<Follow> followEmployeesDeta = em.createNamedQuery("getFollowEmployees", Follow.class)
                                                        .setParameter("login_employee_id", login_employee.getId())
                                                        .getResultList();

        List<Employee> followEmployees = new ArrayList<Employee>();

        for(Follow followEmployeesId : followEmployeesDeta){
            followEmployees.add(em.find(Employee.class, followEmployeesId.getFollow()));
        }

        Long employees_count = (long)em.createNamedQuery("getFollowCount", Long.class)
                                                        .setParameter("employee_id", login_employee.getId())
                                                        .getSingleResult();


        em.close();



        request.setAttribute("followEmployees", followEmployees);
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("employees_count", employees_count);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/followEmployee.jsp");
        rd.forward(request, response);




    }
}
