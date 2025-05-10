package servlet;

import model.User;
import service.GoodsService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "sellman_goods_delete",urlPatterns = "/sellman/goods_delete")
public class SellmanGoodsDeleteServlet extends HttpServlet {
    private GoodsService gService = new GoodsService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String id_str = Integer.toString(id);
        boolean isSuccess = gService.delete(id);
        if(isSuccess) {
            request.setAttribute("msg", "删除成功");

            User u  = (User)request.getSession().getAttribute("user");
            String username = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(username, " ip:"+loginIp+"删除了一个商品,商品id:"+id_str, path);
        }else {
            request.setAttribute("failMsg", "商品下包含订单或推荐条目，无法直接删除商品！");

            User u  = (User)request.getSession().getAttribute("user");
            String username = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(username, " ip:"+loginIp+"尝试删除了一个商品但失败,商品id:"+id_str, path);
        }
        request.getRequestDispatcher("/sellman/goods_list").forward(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
