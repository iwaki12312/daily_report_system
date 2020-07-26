package controllers.employees;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class TestDeta
 */
@WebServlet("/testdeta")
public class TestDeta extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestDeta() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        FileInputStream fi = null;
        InputStreamReader is = null;
        BufferedReader br = null;

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try {

          String path = "C://pleiades/workspace/daily_report_system/csv/testname.csv";

          fi = new FileInputStream(path);
          is = new InputStreamReader(fi);
          br = new BufferedReader(is);

          String line;

          em.getTransaction().begin();

          while ((line = br.readLine()) != null) {

             String[] data = line.split(",");

             Employee e = new Employee();

             e.setName(data[0]);
             e.setCode(data[1]);
             e.setPassword(data[2]);
             e.setAdmin_flag(Integer.parseInt(data[3]));
             e.setDelete_flag(Integer.parseInt(data[4]));
             e.setCreated_at(currentTime);
             e.setUpdated_at(currentTime);
             em.persist(e);

          }

        } catch (Exception e) {
            e.printStackTrace();
          } finally {
            try {
              br.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }


        em.getTransaction().commit();
        em.close();

        request.getSession().setAttribute("flush", "従業員を登録しました。(テストデータ)");
        response.sendRedirect(request.getContextPath() + "/login");

        }
    }
