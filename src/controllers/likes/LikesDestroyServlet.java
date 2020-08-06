package controllers.likes;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class LikesDestroyServlet
 */
@WebServlet("/likes/destroy")
public class LikesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikesDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        Like like = null;

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        try{
            like = em.createNamedQuery("getLikeCheck", Like.class)
                    .setParameter("employee", login_employee)
                    .setParameter("report", r)
                    .getSingleResult();
        }catch (Exception e) {
           e.printStackTrace();
        }

        if(like != null){
            em.remove(like);
        }

        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();

        response.sendRedirect(request.getContextPath() + "/reports/show?id=" + r.getId());

    }

}
