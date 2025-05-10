package servlet;

import model.User;
import service.UserService;

import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="user_close_page_logout", urlPatterns = "/closepage")
public class UserLogoutPageCloseServlet extends HttpServlet {
    private UserService uService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从请求体中读取登出时间

        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        int user_id = loggedInUser.getId();
        //System.out.println(user_id);
        Timestamp logoutTime = new Timestamp(System.currentTimeMillis()); // 获取登出时间
        uService.recordLogoutInfo(user_id,logoutTime);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

