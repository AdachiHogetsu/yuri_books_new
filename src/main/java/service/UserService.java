package service;

import dao.TypeDao;
import dao.UserDao;
import dao.OrderDao;
import model.Goods;
import model.Page;
import model.User;
import utils.DataSourceUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UserService {
    private UserDao uDao = new UserDao();
    private OrderDao oDao = new OrderDao();


    public boolean addSellman(User user){
        try{
            if(uDao.isUsernameExist(user.getUsername())) {
                return false;
            }
            uDao.addSellman(user);
            return true;
        }catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int id){
        try{
            uDao.deleteUser(id);
            return true;
        }catch(SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(User user) {
        try {
            if(uDao.isUsernameExist(user.getUsername())) {
                return false;
            }
            if(uDao.isEmailExist(user.getEmail())) {
                return false;
            }
            uDao.addUser(user);
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public User login(String ue,String password) {
        User user=null;
        try {
            user = uDao.selectByUsernamePassword(ue, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(user!=null) {
            return user;
        }
        try {
            user=uDao.selectByEmailPassword(ue, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(user!=null) {
            return user;
        }
        return null;
    }

    public void recordLoginInfo(int user_id, String username, String loginIp, Timestamp loginTime){
        try{
            uDao.recordLoginInfo(user_id,username,loginIp,loginTime);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void recordLogoutInfo(int user_id, Timestamp logoutTime){
        try{
            uDao.recordLogoutInfo(user_id,logoutTime);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void recordVisitInfo(int user_id, int type_id, long visitTime){
        try{
            uDao.recordVisitInfo(user_id,type_id,visitTime);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public User selectById(int id) {
        User u=null;
        try {
            u = uDao.selectById(id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return u;
    }
    public void updateUserAddress(User user) {
        try {
            uDao.updateUserAddress(user);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updatePwd(User user) {
        try {
            uDao.updatePwd(user);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Page getSellmanPage(int pageNumber) {
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int totalCount = 0;
        try {
            totalCount = uDao.getCountOfSellmanByTypeID();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(8, totalCount);
        List list=null;
        try {
            list = uDao.selectSellman( pageNumber, 8);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setList(list);
        return p;
    }

    public double calculateKpi(int selltypeid){
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection();
            return oDao.sumPriceByTypeId(con,selltypeid);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (con != null) {
                try {
                    con.close(); // 关闭连接
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

}
