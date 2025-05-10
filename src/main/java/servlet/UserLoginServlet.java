package servlet;

import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;


@WebServlet(name = "user_login",urlPatterns = "/user_login")
public class UserLoginServlet extends HttpServlet {
    private UserService uService = new UserService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ue = request.getParameter("ue");
        String password = request.getParameter("password");
        User user = uService.login(ue, password);
        if(user==null) {
            request.setAttribute("failMsg", "用户名、邮箱或者密码错误，请重新登录！");
            request.getRequestDispatcher("/user_login.jsp").forward(request, response);
        }else {
            String loginIp = request.getRemoteAddr(); // 获取登录IP
            Timestamp loginTime = new Timestamp(System.currentTimeMillis()); // 获取登录时间

            // 将登录信息写入数据库
            uService.recordLoginInfo(user.getId(), user.getUsername(), loginIp, loginTime);

            // 改动，直接跳转到后台而管理员不具备前台购买能力
            if(user.isIsadmin()){
                String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
                File logFolder = new File(path);
                if (!logFolder.exists()) {
                    logFolder.mkdirs(); // 如果文件夹不存在，则递归创建文件夹
                }
                // 生成专属的日志文件名
                String logFileName = path+ user.getUsername() + "_log.txt";
                // 检查日志文件是否存在
                File logFile = new File(logFileName);
                if (!logFile.exists()) {
                    try {
                        // 创建新的日志文件
                        logFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                request.getSession().setAttribute("user", user);

                request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
            }
            else if(user.isIssellman()){
                String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
                File logFolder = new File(path);
                if (!logFolder.exists()) {
                    logFolder.mkdirs(); // 如果文件夹不存在，则递归创建文件夹
                }
                // 生成专属的日志文件名
                String logFileName = path+ user.getUsername() + "_log.txt";
                // 检查日志文件是否存在
                File logFile = new File(logFileName);
                if (!logFile.exists()) {
                    try {
                        // 创建新的日志文件
                        logFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                request.getSession().setAttribute("user", user);

                request.getRequestDispatcher("/sellman/index.jsp").forward(request, response);
            }
            request.getSession().setAttribute("user", user);
            request.setAttribute("user_id", user.getId());
            request.getRequestDispatcher("/user_center.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}