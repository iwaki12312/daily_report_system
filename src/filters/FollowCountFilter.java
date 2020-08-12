package filters;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet Filter implementation class followCountFilter
 */
@WebFilter("/*")
public class FollowCountFilter implements Filter {

    /**
     * Default constructor.
     */
    public FollowCountFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String servlet_path = ((HttpServletRequest)request).getServletPath();

        if(!servlet_path.matches("/css.*")) {

            HttpSession session = ((HttpServletRequest)request).getSession();

            if(!servlet_path.equals("/login")) {

                EntityManager em = DBUtil.createEntityManager();

                Employee e = (Employee)session.getAttribute("login_employee");

                if(e != null){

                long followCount = (long)em.createNamedQuery("getFollowCount", Long.class)
                        .setParameter("employee", e)
                        .getSingleResult();

                long followerCount = (long)em.createNamedQuery("getFollowerCount", Long.class)
                        .setParameter("employee", e)
                        .getSingleResult();

                request.setAttribute("followCount", followCount);
                request.setAttribute("followerCount", followerCount);

                }

            }

        }
        chain.doFilter(request, response);

    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
