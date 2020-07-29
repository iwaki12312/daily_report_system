package controllers.attendance;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
@WebServlet("/testattendance")
public class TestAttendance extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestAttendance() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        em.getTransaction().begin();

        String setDate = "2020-08-06 09:00:00.000000000";
        Timestamp setNowData = Timestamp.valueOf(setDate);

        String in = "2020-08-06 09:00:00.000000000";
        Timestamp inData = Timestamp.valueOf(in);

        String out = "2020-08-06 18:00:00.000000000";
        Timestamp outData = Timestamp.valueOf(out);

        Calendar setCalendar = Calendar.getInstance();
        setCalendar.setTime(setNowData);

        Calendar inCalendar = Calendar.getInstance();
        inCalendar.setTime(inData);

        Calendar outCalendar = Calendar.getInstance();
        outCalendar.setTime(outData);




        for(int i = 0; i < 1000; i++){

        Attendance a = new Attendance();
        setCalendar.add(Calendar.DAY_OF_MONTH, -1);
        inCalendar.add(Calendar.DAY_OF_MONTH, -1);
        outCalendar.add(Calendar.DAY_OF_MONTH, -1);


        date = setCalendar.getTime();

        String str1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inCalendar.getTime());
        Timestamp inTime = Timestamp.valueOf(str1);

        String str2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(outCalendar.getTime());
        Timestamp outTime = Timestamp.valueOf(str2);


        a.setEmployee(login_employee);
        a.setDate(dateFormat.format(date));
        a.setInTime(inTime);
        a.setOutTime(outTime);
        a.setCreated_at(currentTime);
        a.setUpdated_at(currentTime);
        em.persist(a);
        }



        em.getTransaction().commit();
        em.close();

        request.getSession().setAttribute("flush", "出勤退勤時刻を登録しました。(テストデータ)");
        response.sendRedirect(request.getContextPath() + "/login");


    }
}
