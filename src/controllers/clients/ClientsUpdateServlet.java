package controllers.clients;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Client;
import models.validators.ClientValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ClientsUpdateServlet
 */
@WebServlet("/clients/update")
public class ClientsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientsUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Client c = em.find(Client.class, (Integer)(request.getSession().getAttribute("client_id")));

            // 現在の値と異なる取引先コードが入力されていたら
            // 重複チェックを行う指定をする
            Boolean code_duplicate_check = true;
            if(c.getCode().equals(request.getParameter("code"))) {
                code_duplicate_check = false;
            } else {
                c.setCode(request.getParameter("code"));
            }

            // 現在の値と異なる住所が入力されていたら
            // 重複チェックを行う指定をする
            Boolean address_duplicate_check = true;
            if(c.getAddress().equals(request.getParameter("address"))) {
                address_duplicate_check = false;
            } else {
                c.setAddress(request.getParameter("address"));
            }

            // 現在の値と異なる電話番号が入力されていたら
            // 重複チェックを行う指定をする
            Boolean tell_duplicate_check = true;
            if(c.getTell().equals(request.getParameter("tell"))) {
                tell_duplicate_check = false;
            } else {
                c.setTell(request.getParameter("tell"));
            }



            c.setName(request.getParameter("name"));
            c.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            c.setDelete_flag(0);

            List<String> errors = ClientValidator.validate(c,code_duplicate_check,
                                                           address_duplicate_check,
                                                           tell_duplicate_check);
            if(errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("client", c);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/clients/edit.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                request.getSession().removeAttribute("client_id");

                response.sendRedirect(request.getContextPath() + "/clients/index");
            }
        }
    }
}
