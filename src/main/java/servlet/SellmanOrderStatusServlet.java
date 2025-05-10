package servlet;

import model.User;
import service.OrderService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "sellman_order_status",urlPatterns = "/sellman/order_status")
public class SellmanOrderStatusServlet extends HttpServlet {
    private OrderService oService = new OrderService();
    private LogUtils Log = new LogUtils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        oService.updateStatus(id, status);

        User u  = (User)request.getSession().getAttribute("user");
        String username = u.getUsername();
        String loginIp = request.getRemoteAddr();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username, " ip:"+loginIp+"更新了订单状态,订单id:"+id+"更新后的状态:"+status, path);

        response.sendRedirect("/sellman/order_list?status="+status);
    }
}
