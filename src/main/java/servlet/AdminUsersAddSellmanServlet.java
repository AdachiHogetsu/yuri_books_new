package servlet;

import model.User;
import model.Type;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.UserService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet(name = "sellman_user_register",urlPatterns = "/admin/sellman_user_rigister")
public class AdminUsersAddSellmanServlet extends HttpServlet{
    private UserService uService = new UserService();
    private LogUtils Log = new LogUtils();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int typeid = Integer.parseInt(request.getParameter("typeid"));

        user.setPassword(password);
        user.setUsername(username);
        user.setIssellman(true);
        user.setSelltypeid(typeid);

        if(uService.addSellman(user)) {
            request.setAttribute("msg", "添加成功");

            User u  = (User)request.getSession().getAttribute("user");
            String uname = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(uname, " ip:"+loginIp+"添加了一个销售人员,名称:"+username+"销售商品类型id:"+typeid, path);

            request.getRequestDispatcher("sellman_add.jsp").forward(request, response);
        }else {
            request.setAttribute("msg", "用户名重复，请重新填写！");

            User u  = (User)request.getSession().getAttribute("user");
            String uname = u.getUsername();
            String loginIp = request.getRemoteAddr();
            String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
            path+=username+".txt";
            Log.log(uname, " ip:"+loginIp+"尝试添加一个销售人员但是失败了", path);

            request.getRequestDispatcher("sellman_add.jsp").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
