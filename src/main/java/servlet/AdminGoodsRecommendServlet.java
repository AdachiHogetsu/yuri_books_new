package servlet;

import model.User;
import service.GoodsService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "admin_goods_recommend",urlPatterns = "/admin/goods_recommend")
public class AdminGoodsRecommendServlet extends HttpServlet {
    private GoodsService gService = new GoodsService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        String method = request.getParameter("method");
        int typeTarget=Integer.parseInt(request.getParameter("typeTarget"));
        if(method.equals("add")) {
            gService.addRecommend(id, typeTarget,typeId);

            User u  = (User)request.getSession().getAttribute("user");
            String username = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(username, " ip:"+loginIp+"将商品加入了推荐类型,商品id:"+id+",推荐类型:"+typeTarget, path);

        }else {
            gService.removeRecommend(id, typeTarget);

            User u  = (User)request.getSession().getAttribute("user");
            String username = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(username, " ip:"+loginIp+"将商品移出了推荐类型,商品id:"+id+",推荐类型:"+typeTarget, path);

        }
        request.getRequestDispatcher("/admin/goods_list").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
