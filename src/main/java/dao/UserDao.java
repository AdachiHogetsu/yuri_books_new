package dao;

import model.Goods;
import model.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.DataSourceUtils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UserDao {
    public void addUser(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user(username,email,password,name,phone,address,isadmin,isvalidate,issellman) values(?,?,?,?,?,?,?,?,?)";
        r.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getName(), user.getPhone(), user.getAddress(), user.isIsadmin(), user.isIsvalidate(), user.isIssellman());
    }

    public void addSellman(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user(username,password,email,isadmin,isvalidate,issellman,selltypeid) values(?,?,?,?,?,?,?)";
        r.update(sql, user.getUsername(), user.getPassword(), 0, false, false, true, user.getSelltypeid());
    }

    public void deleteUser(int id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "delete from user where id=?";
        r.update(sql, id);
    }

    public boolean isUsernameExist(String username) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where username = ?";
        User u = r.query(sql, new BeanHandler<User>(User.class), username);
        if (u == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmailExist(String email) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where email = ?";
        User u = r.query(sql, new BeanHandler<User>(User.class), email);
        if (u == null) {
            return false;
        } else {
            return true;
        }
    }

    public User selectByUsernamePassword(String username, String password) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where username=? and password=?";
        return r.query(sql, new BeanHandler<User>(User.class), username, password);
    }

    public User selectByEmailPassword(String email, String password) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where email=? and password=?";
        return r.query(sql, new BeanHandler<User>(User.class), email, password);
    }

    public User selectById(int id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where id=?";
        return r.query(sql, new BeanHandler<User>(User.class), id);
    }

    public void updateUserAddress(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update user set name = ?,phone=?,address=? where id = ?";
        r.update(sql, user.getName(), user.getPhone(), user.getAddress(), user.getId());
    }

    public void updatePwd(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update user set password = ? where id = ?";
        r.update(sql, user.getPassword(), user.getId());
    }

    public int selectUserCount() throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from user";
        return r.query(sql, new ScalarHandler<Long>()).intValue();
    }

    public List selectUserList(int pageNo, int pageSize) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user limit ?,?";
        return r.query(sql, new BeanListHandler<User>(User.class), (pageNo - 1) * pageSize, pageSize);
    }

    public int getCountOfSellmanByTypeID() throws SQLException {
        String sql = "";
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        sql = "select count(*) from user where issellman=1";
        return r.query(sql, new ScalarHandler<Long>()).intValue();

    }

    public List<User> selectSellman(int pageNumber, int pageSize) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        //当不添加推荐类型限制的时候
        String sql = " select g.id,g.username,g.selltypeid from user g where g.issellman=1 order by g.id limit ?,?";
        return r.query(sql, new BeanListHandler<User>(User.class), (pageNumber - 1) * pageSize, pageSize);

    }
    public void recordLoginInfo(int user_id, String username, String loginIp, Timestamp loginTime) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user_data(user_id,username,login_time,ip_address ) values(?,?,?,?)"+
                "ON DUPLICATE KEY UPDATE username=?, login_time=?, ip_address=?";
        r.update(sql, user_id,username,loginTime,loginIp, username, loginTime, loginIp);
    }
    public void recordLogoutInfo(int user_id,Timestamp logoutTime) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update user_data set logout_time = ? where user_id = ?";
        r.update(sql, logoutTime, user_id);
    }
    public void recordVisitInfo(int user_id,int type_id, long visitTime) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String insertSql = "INSERT IGNORE INTO user_visit(user_id,type_id,visit_time) VALUES(?,?,?)";
        String updateSql = "UPDATE user_visit SET visit_time = visit_time + ? WHERE user_id = ? AND  type_id = ?";

        int insertedRows = r.update(insertSql, user_id, type_id, visitTime);
        if (insertedRows == 0) {
            r.update(updateSql, visitTime,user_id,type_id);
        }
    }
}