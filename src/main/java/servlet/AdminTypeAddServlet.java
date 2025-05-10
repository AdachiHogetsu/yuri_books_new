package servlet;

import model.Type;
import model.User;
import service.TypeService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "admin_type_add",urlPatterns = "/admin/type_add")
public class AdminTypeAddServlet extends HttpServlet {
    private TypeService tService = new TypeService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        tService.insert(new Type(name));

        User u  = (User)request.getSession().getAttribute("user");
        String username = u.getUsername();
        String loginIp = request.getRemoteAddr();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username, " ip:"+loginIp+"新加入一种商品类型,商品类型名称:"+name, path);

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
