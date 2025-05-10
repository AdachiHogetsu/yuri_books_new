package servlet;

import model.Goods;
import model.Page;
import model.Type;
import model.User;
import service.GoodsService;
import service.TypeService;
import utils.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "sellman_goods_list",urlPatterns = "/sellman/goods_list")
public class SellmanGoodsListServlet extends HttpServlet {
    private GoodsService gService = new GoodsService();
    private TypeService tService = new TypeService();
    private LogUtils Log = new LogUtils();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int type = 0;//推荐类型
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        int type_id = loggedInUser.getSelltypeid();
        if(request.getParameter("type") != null) {
            type=Integer.parseInt(request.getParameter("type") ) ;
        }
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
        Page p = gService.getSellmanGoodsRecommendPage(type, pageNumber, type_id);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = gService.getSellmanGoodsRecommendPage(type, pageNumber, type_id);
            }
        }
        List<Integer> amountValues = new ArrayList<>();
        for (Object obj : p.getList()) {
            Goods g = (Goods) obj;
            int amount = gService.calculateAmount(g.getId());
            amountValues.add(amount);
        }
        List<Integer> typeIds = new ArrayList<>();
        for (Object obj : p.getList()) {
            Goods g = (Goods) obj;
            Type t = tService.selectIDByTypeName(g.getType().getName());
            int typeId = t.getId();
            typeIds.add(typeId);
        }


        request.setAttribute("p", p);
        request.setAttribute("type", type);
        request.setAttribute("amountValues",amountValues);
        request.setAttribute("typeIds", typeIds);

        User u  = (User)request.getSession().getAttribute("user");
        String username = u.getUsername();
        String loginIp = request.getRemoteAddr();
        String path = this.getServletContext().getRealPath("/WEB-INF/logs_op/");
        path+=username+".txt";
        Log.log(username, " ip:"+loginIp+"打开了商品列表,页面类型"+type, path);

        request.getRequestDispatcher("/sellman/goods_list.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
