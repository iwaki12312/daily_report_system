package controllers.clients;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Client;
import utils.DBUtil;

/**
 * Servlet implementation class ClientsIndexServlet
 */
@WebServlet("/clients/index")
public class ClientsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }

        List<Client> clients = em.createNamedQuery("getAllClients", Client.class)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        Long clients_count = (long)em.createNamedQuery("getClientsCount", Long.class)
                  .getSingleResult();

        request.setAttribute("clients", clients);
        request.setAttribute("clients_count", clients_count);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/clients/index.jsp");
        rd.forward(request, response);
    }

}
