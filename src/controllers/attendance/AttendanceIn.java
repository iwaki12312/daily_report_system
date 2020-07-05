package controllers.attendance;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class AttendanceIn
 */
@WebServlet("/attendance/in")
public class AttendanceIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendanceIn() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Attendance b = null;

        try{b = em.createNamedQuery("getTimes", Attendance.class)
                .setParameter("employee", login_employee)
                .setParameter("date", dateFormat.format(date))
                .getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }


        if(b == null){

            Attendance a = new Attendance();

            a.setEmployee(login_employee);

            a.setDate(dateFormat.format(date));

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            a.setInTime(currentTime);
            a.setCreated_at(currentTime);
            a.setUpdated_at(currentTime);

            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "出勤時刻を登録しました");
            response.sendRedirect(request.getContextPath() + "/login");

        }else if(b.getInTime() == null){

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            b.setInTime(currentTime);
            b.setUpdated_at(currentTime);

            em.getTransaction().begin();
            em.persist(b);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "出勤時刻を登録しました。");
            response.sendRedirect(request.getContextPath() + "/login");
        }else{
            request.getSession().setAttribute("flush", "本日の出勤時刻は既に登録されています");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
