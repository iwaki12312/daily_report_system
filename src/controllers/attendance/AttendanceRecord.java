package controllers.attendance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class AttendanceRecord
 */
@WebServlet("/attendance/record")
public class AttendanceRecord extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AttendanceRecord() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Employee employee = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        String context_path = ((HttpServletRequest)request).getContextPath();

        if(login_employee.getPosition_flag() > 0 || (login_employee.getId() == employee.getId())){

        Date currentDate = new Date();
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM");


        String m = request.getParameter("month");
        String y = request.getParameter("year");

            if(Integer.parseInt(m) <= 0 || Integer.parseInt(m) > 12 ){
                m = dateFormatMonth.format(currentDate);
            }

            if(Integer.parseInt(y) < 2010 || Integer.parseInt(y) > Integer.parseInt(dateFormatYear.format(currentDate))){
                y = dateFormatYear.format(currentDate);
            }

        String date = y + "-" + m;

        List<Attendance> records = em.createNamedQuery("getAttendanceRecords", Attendance.class)
                       .setParameter("date", "%" + date + "%")
                       .setParameter("employee", employee)
                       .getResultList();

        request.setAttribute("records", records);
        request.setAttribute("employee", employee);
        request.setAttribute("month", m);
        request.setAttribute("year", y);
        request.setAttribute("currentYear", dateFormatYear.format(currentDate));

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/attendance/attendanceRecord.jsp");
        rd.forward(request, response);

        }else{

            ((HttpServletResponse)response).sendRedirect(context_path + "/");
            return;
        }

    }

}
