package controllers.follow;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowRemove
 */
@WebServlet("/follow/remove")
public class FollowRemove extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowRemove() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        String search = request.getParameter("search");
        String followsearch = request.getParameter("followsearch");
        String followersearch = request.getParameter("followersearch");


        int reportId = 0;
        if(request.getParameter("reportId") != null){
        reportId =  Integer.parseInt(request.getParameter("reportId"));
        }

        int followId = 0;
        if(request.getParameter("followId") != null){
        followId =  Integer.parseInt(request.getParameter("followId"));
        }

        int followerId = 0;
        if(request.getParameter("followerId") != null){
        followerId =  Integer.parseInt(request.getParameter("followerId"));
        }

        int indexId = 0;
        if(request.getParameter("indexId") != null){
        indexId =  Integer.parseInt(request.getParameter("indexId"));
        }

        Follow b = null;

        if(reportId > 0){

            Report r = em.find(Report.class, reportId);
            Employee employee = r.getEmployee();


            try{
                b = em.createNamedQuery("getFollows", Follow.class)
                .setParameter("employee", login_employee)
                .setParameter("follow", employee)
                .getSingleResult();

            }catch (Exception e) {
                e.printStackTrace();
            }

            if(b != null){

                em.getTransaction().begin();
                em.remove(b);
                em.getTransaction().commit();
                em.close();

                response.sendRedirect(request.getContextPath() + "/reports/show?id=" + r.getId());
            }else{
                em.close();
                response.sendRedirect(request.getContextPath() + "/reports/show?id=" + r.getId());
            }
        }else if(followId > 0){

            Employee employee = em.find(Employee.class, followId);

            try{
                b = em.createNamedQuery("getFollows", Follow.class)
                .setParameter("employee", login_employee)
                .setParameter("follow", employee)
                .getSingleResult();

            }catch (Exception e) {
                e.printStackTrace();
            }

            if(b != null){

                em.getTransaction().begin();
                em.remove(b);
                em.getTransaction().commit();
                em.close();

                response.sendRedirect(request.getContextPath() + "/follow/followemployee");
            }else{
                em.close();
                response.sendRedirect(request.getContextPath() + "/follow/followemployee");
            }
        }else if(followerId > 0){

            Employee employee = em.find(Employee.class, followerId);

            try{
                b = em.createNamedQuery("getFollows", Follow.class)
                .setParameter("employee", login_employee)
                .setParameter("follow", employee)
                .getSingleResult();

            }catch (Exception e) {
                e.printStackTrace();
            }

            if(b != null){

                em.getTransaction().begin();
                em.remove(b);
                em.getTransaction().commit();
                em.close();

                response.sendRedirect(request.getContextPath() + "/follow/followeremployee");
            }else{
                em.close();
                response.sendRedirect(request.getContextPath() + "/follow/followeremployee");
            }
        }else if(indexId > 0){

            Employee employee = em.find(Employee.class, indexId);

            try{
                b = em.createNamedQuery("getFollows", Follow.class)
                .setParameter("employee", login_employee)
                .setParameter("follow", employee)
                .getSingleResult();

            }catch (Exception e) {
                e.printStackTrace();
            }

            if(b != null){

                em.getTransaction().begin();
                em.remove(b);
                em.getTransaction().commit();
                em.close();

                response.sendRedirect(request.getContextPath() + "/follow/index?page=" + page
                        +"&search=" + search + "&followsearch=" + followsearch + "&followersearch=" + followersearch);
            }else{
                em.close();
                response.sendRedirect(request.getContextPath() + "/follow/index?page=" + page
                        +"&search=" + search + "&followsearch=" + followsearch + "&followersearch=" + followersearch);
            }
        }

    }

}
