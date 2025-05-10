package servlet;

import dao.OrderDao;
import model.Page;
import model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.OrderService;
import service.UserService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet(name = "admin_sellman_kpi_list",urlPatterns = "/admin/sellman_kpi_list")
public class AdminSellmanKpiListServlet extends HttpServlet{
    private UserService uService = new UserService();
    private OrderService oService = new OrderService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            }
            catch (Exception e)
            {

            }

        }
        if(pageNumber<=0)
            pageNumber=1;
        Page p = uService.getSellmanPage(pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = uService.getSellmanPage(pageNumber);
            }
        }
        // 计算每个用户对象的KPI值
        List<Double> kpiValues = new ArrayList<>();
        for (Object obj : p.getList()) {
            User u = (User)obj;
            double kpi = uService.calculateKpi(u.getSelltypeid());
            kpiValues.add(kpi);
        }

        //绘制折线图1/3
        Map<Integer,List<Integer>> amountList = new HashMap<>();
        for (Object obj : p.getList()) {
            User u = (User)obj;
            amountList.put(u.getSelltypeid(),oService.getAmountListByTypeID(u.getSelltypeid()));
        }

        Map<Integer,List<Integer>> orderList = new HashMap<>();
        for (Object obj : p.getList()) {
            User u = (User)obj;
            orderList.put(u.getSelltypeid(),oService.getOrderListByTypeID(u.getSelltypeid()));
        }

        //绘制折线图3/3
        Map<Integer,List<String>> datetimeList = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : orderList.entrySet()) {
            datetimeList.put(entry.getKey(), oService.getDatetimetListByOrderIDs(entry.getValue()));
        }

//        for (Map.Entry<Integer, List<String>> entry : datetimeList.entrySet()) {
//            Integer key = entry.getKey();
//            List<String> valueList = entry.getValue();
//
//            System.out.println("Key: " + key);
//            System.out.println("Values:");
//            for (String value : valueList) {
//                System.out.println(value);
//            }
//        }

        request.setAttribute("p", p);
        request.setAttribute("kpi", kpiValues);
        request.getSession().setAttribute("amountList", amountList);
        request.getSession().setAttribute("datetimeList", datetimeList);

        User u  = (User)request.getSession().getAttribute("user");
        String username = u.getUsername();
        String loginIp = request.getRemoteAddr();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username, " ip:"+loginIp+"打开了销售人员营销业绩表", path);

        request.getRequestDispatcher("/admin/sellman_kpi_list.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
