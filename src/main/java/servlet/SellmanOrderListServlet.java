package servlet;

import model.Page;
import model.User;
import service.OrderService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "sellman_order_list",urlPatterns = "/sellman/order_list")
public class SellmanOrderListServlet extends HttpServlet {
    private OrderService oService = new OrderService();

    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        if(request.getParameter("status") != null) {
            status=Integer.parseInt(request.getParameter("status") ) ;
        }
        request.setAttribute("status", status);
        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            }
            catch (Exception e)
            {

            }
        }
        if(pageNumber<=0)
            pageNumber=1;
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        int typeId = loggedInUser.getSelltypeid();

        Page p = oService.getSellmanOrderPage(status,pageNumber,typeId);

        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = oService.getSellmanOrderPage(status,pageNumber,typeId);
            }
        }

        request.setAttribute("p", p);

        User u  = (User)request.getSession().getAttribute("user");
        String loginIp = request.getRemoteAddr();
        String username = u.getUsername();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username," ip:"+loginIp + "打开了订单列表,订单状态:"+status, path);

        request.getRequestDispatcher("/sellman/order_list.jsp").forward(request, response);
    }
}
