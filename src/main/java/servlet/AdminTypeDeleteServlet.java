package servlet;

import model.Page;
import model.User;
import service.TypeService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "admin_type_delete",urlPatterns = "/admin/type_delete")
public class AdminTypeDeleteServlet extends HttpServlet {
    private TypeService tService = new TypeService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isSuccess = tService.delete(id);
        if(isSuccess) {
            request.setAttribute("msg", "删除成功");

            User u  = (User)request.getSession().getAttribute("user");
            String username = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(username, " ip:"+loginIp+"成功删除一种商品类型,类型id:"+id, path);

        }else {
            request.setAttribute("failMsg", "类目下包含商品，无法直接删除类目！");

            User u  = (User)request.getSession().getAttribute("user");
            String username = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(username, " ip:"+loginIp+"尝试删除一种商品类型，但失败,类型id:"+id, path);

        }
        request.getRequestDispatcher("/admin/type_list").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
