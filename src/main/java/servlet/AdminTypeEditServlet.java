package servlet;

import model.Type;
import model.User;
import org.apache.commons.beanutils.BeanUtils;
import service.TypeService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "admin_type_edit",urlPatterns = "/admin/type_edit")
public class AdminTypeEditServlet extends HttpServlet {
    private TypeService tService = new TypeService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Type t = new Type();
        try {
            BeanUtils.copyProperties(t, request.getParameterMap());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tService.update(t);

        User u  = (User)request.getSession().getAttribute("user");
        String username = u.getUsername();
        String loginIp = request.getRemoteAddr();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username, " ip:"+loginIp+"更新了商品类型信息,类型id:"+t.getId(), path);

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
