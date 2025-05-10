package dao;

import model.*;
import org.apache.commons.dbutils.*;
import org.apache.tomcat.util.buf.StringUtils;
import utils.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.dbutils.handlers.*;


public class OrderDao {
    public void insertOrder(Connection con, Order order) throws SQLException {
        QueryRunner r = new QueryRunner();
        String sql = "insert into `orders`(total,amount,status,paytype,name,phone,address,datetime,user_id) values(?,?,?,?,?,?,?,?,?)";
        r.update(con,sql,
                order.getTotal(),order.getAmount(),order.getStatus(),
                order.getPaytype(),order.getName(),order.getPhone(),
                order.getAddress(),order.getDatetime(),order.getUser().getId() );
    }
    public int getLastInsertId(Connection con) throws SQLException {
        QueryRunner r = new QueryRunner();
        String sql = "select last_insert_id()";
        BigInteger bi = r.query(con, sql,new ScalarHandler<BigInteger>());
        return Integer.parseInt(bi.toString());
    }
    public void insertOrderItem(Connection con, OrderItem item) throws SQLException {
        QueryRunner r = new QueryRunner();
        String sql ="insert into order_item(price,amount,goods_id,order_id,type_id) values(?,?,?,?,?)";
        r.update(con,sql,item.getPrice(),item.getAmount(),item.getGoods().getId(),item.getOrder().getId(),item.getGoods().getType().getId());
    }

    public double sumPriceByTypeId(Connection con, int typeId) throws SQLException {
        double totalPrice = 0;
        String sql = "SELECT price , amount FROM order_item WHERE type_id = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, typeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalPrice += resultSet.getDouble("price")*resultSet.getDouble("amount");
                }
            }
        }
        return totalPrice;
    }

    public List<Order> selectAll(int userid) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from `orders` where user_id=? order by datetime desc";
        return r.query(sql, new BeanListHandler<Order>(Order.class),userid);
    }
    public List<OrderItem> selectAllItem(int orderid) throws SQLException{
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select i.id,i.price,i.amount,g.name from order_item i,goods g where order_id=? and i.goods_id=g.id";
        return r.query(sql, new BeanListHandler<OrderItem>(OrderItem.class),orderid);
    }
    public int getOrderCount(int status) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "";
        if(status==0) {
            sql = "select count(*) from `orders`";
            return r.query(sql, new ScalarHandler<Long>()).intValue();
        }else {
            sql = "select count(*) from `orders` where status=?";
            return r.query(sql, new ScalarHandler<Long>(),status).intValue();
        }
    }
    public int getSellmanOrderCount(int status, List<Integer>orderIds) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "";
        if (status == 0) {
            if (orderIds.isEmpty()) {
                sql = "select count(*) from `orders`";
                return r.query(sql, new ScalarHandler<Long>()).intValue();
            } else {
                StringBuilder orderIdString = new StringBuilder();
                for (Integer orderId : orderIds) {
                    orderIdString.append(orderId).append(",");
                }
                orderIdString.deleteCharAt(orderIdString.length() - 1); // Remove the last comma
                sql = "select count(*) from `orders` where id in (" + orderIdString.toString() + ")";
                return r.query(sql, new ScalarHandler<Long>()).intValue();
            }
        } else {
            if (orderIds.isEmpty()) {
                sql = "select count(*) from `orders` where status=?";
                return r.query(sql, new ScalarHandler<Long>(), status).intValue();
            } else {
                StringBuilder orderIdString = new StringBuilder();
                for (Integer orderId : orderIds) {
                    orderIdString.append(orderId).append(",");
                }
                orderIdString.deleteCharAt(orderIdString.length() - 1); // Remove the last comma
                sql = "select count(*) from `orders` where status=? and id in (" + orderIdString.toString() + ")";
                return r.query(sql, new ScalarHandler<Long>(), status).intValue();
            }
        }
    }
    public List<Integer> selectSellmanOrdersId(int typeId) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select order_id from `order_item` where type_id=?";
        return r.query(sql, new ColumnListHandler<Integer>(),typeId);
    }

    public List<Integer> selectSellmanType(int typeId) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select order_id from `order_item` where type_id=?";
        return r.query(sql, new ColumnListHandler<Integer>(),typeId);
    }

    public List<Order> selectOrderList(int status, int pageNumber, int pageSize) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        if(status==0) {
            String sql = "select o.id,o.total,o.amount,o.status,o.paytype,o.name,o.phone,o.address,o.datetime,u.username from `orders` o,user u where o.user_id=u.id order by o.datetime desc limit ?,?";
            return r.query(sql, new BeanListHandler<Order>(Order.class), (pageNumber-1)*pageSize,pageSize );
        }else {
            String sql = "select o.id,o.total,o.amount,o.status,o.paytype,o.name,o.phone,o.address,o.datetime,u.username from `orders` o,user u where o.user_id=u.id and o.status=? order by o.datetime desc limit ?,?";
            return r.query(sql, new BeanListHandler<Order>(Order.class),status, (pageNumber-1)*pageSize,pageSize );
        }
    }
    public List<Order> selectSellmanOrderList(int status, int pageNumber, int pageSize, List<Integer>orderIds) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        StringBuilder sqlBuilder = new StringBuilder("SELECT o.id, o.total, o.amount, o.status, o.paytype, o.name, o.phone, o.address, o.datetime, u.username ");
        sqlBuilder.append("FROM `orders` o, user u ");
        sqlBuilder.append("WHERE o.user_id = u.id ");

        if (orderIds != null && !orderIds.isEmpty()) {
            sqlBuilder.append("AND o.id IN (");
            for (int i = 0; i < orderIds.size(); i++) {
                if (i > 0) {
                    sqlBuilder.append(",");
                }
                sqlBuilder.append("?");
            }
            sqlBuilder.append(") ");
        }

        if (status != 0) {
            sqlBuilder.append("AND o.status = ? ");
        }

        sqlBuilder.append("ORDER BY o.datetime DESC LIMIT ?, ?");

        String sql = sqlBuilder.toString();

        List<Object> params = new ArrayList<>();

        if (orderIds != null && !orderIds.isEmpty()) {
            params.addAll(orderIds);
        }

        if (status != 0) {
            params.add(status);
        }

        params.add((pageNumber - 1) * pageSize);
        params.add(pageSize);

        return r.query(sql, new BeanListHandler<>(Order.class), params.toArray());
    }

    public void updateStatus(int id,int status) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql ="update `orders` set status=? where id = ?";
        r.update(sql,status,id);
    }
    public void updateTypeid(int goods_id, int type_id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql ="update `order_item` set type_id=? where goods_id = ?";
        r.update(sql,type_id,goods_id);
    }
    public void deleteOrder(Connection con ,int id) throws SQLException {
        QueryRunner r = new QueryRunner();
        String sql ="delete from `orders` where id = ?";
        r.update(con,sql,id);
    }
    public void deleteOrderItem(Connection con ,int id) throws SQLException {
        QueryRunner r = new QueryRunner();
        String sql ="delete from order_item where order_id=?";
        r.update(con,sql,id);
    }
    public int getUserIdByOrderId(int orderId) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "SELECT user_id FROM `orders` WHERE id=?";

        Integer userId = (Integer) runner.query(sql, new ScalarHandler<>(), orderId);

        // 如果 userId 为 null，表示没有找到对应的订单
        if (userId == null) {
            throw new SQLException("订单不存在");
        }

        return userId;
    }

    public List<Integer> getAmountListByTypeID(int type_id) throws SQLException{
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select amount from `order_item` where type_id=?";
        return r.query(sql, new ColumnListHandler<Integer>(),type_id);
    }

    public List<Integer> getOrderListByTypeID(int type_id) throws SQLException{
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select order_id from `order_item` where type_id=?";
        return r.query(sql, new ColumnListHandler<Integer>(),type_id);
    }

    public String getDateTimeByOrderID(int order_id) throws SQLException{
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select datetime from `orders` where id=?";
        return r.query(sql, new ScalarHandler<>(),order_id).toString();
    }

}
