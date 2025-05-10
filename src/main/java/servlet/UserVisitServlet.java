package servlet;

import model.User;
import service.UserService;

import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name ="user_visit", urlPatterns ="/visit")
public class UserVisitServlet extends HttpServlet {
    private Timestamp startVisitTime;
    private UserService uService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 记录访问开始时间
        startVisitTime = new Timestamp(System.currentTimeMillis());
        //System.out.println("starttime");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //System.out.println("endtime");


        String goodsTypeid = request.getParameter("goodsTypeid");
        String userId = request.getParameter("userId");
        int goodsTypeidInt = Integer.parseInt(goodsTypeid);
        int userIdInt = Integer.parseInt(userId);


        //System.out.println("user_id:"+userIdInt);
        //System.out.println("type_id:"+goodsTypeidInt);

        Timestamp endVisitTime = new Timestamp(System.currentTimeMillis());
        long visitTime = (endVisitTime.getTime() - startVisitTime.getTime())/1000;
        uService.recordVisitInfo(userIdInt,goodsTypeidInt,visitTime);


    }

}




