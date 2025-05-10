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

@WebServlet(name = "admin_order_delete",urlPatterns = "/admin/order_delete")
public class AdminOrderDeleteServlet extends HttpServlet {
    private OrderService oService = new OrderService();
    private LogUtils Log = new LogUtils();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        oService.delete(id);

        User u  = (User)request.getSession().getAttribute("user");
        String username = u.getUsername();
        String loginIp = request.getRemoteAddr();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username, " ip:"+loginIp+"将订单信息删除了,订单id:"+id, path);

        request.getRequestDispatcher("/admin/order_list").forward(request, response);
    }
}
