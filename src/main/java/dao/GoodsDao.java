package dao;

import model.Goods;
import model.Recommend;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;
import utils.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GoodsDao {
    //select g.id,g.name,g.cover,g.price,t.name typename from recommend r,goods g,type t where type=2 and r.goods_id=g.id and g.type_id=t.id
    public List<Map<String,Object>> getGoodsList(int recommendType) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select g.id,g.name,g.cover,g.price,t.name typename from recommend r,goods g,type t where type=? and r.goods_id=g.id and g.type_id=t.id";
        return r.query(sql, new MapListHandler(),recommendType);
    }

    public List<Map<String,Object>> getScrollGood()throws SQLException{
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select g.id,g.name,g.cover,g.price  from recommend r,goods g where r.goods_id=g.id";
        return r.query(sql, new MapListHandler());
    }
    public List<Goods> selectGoodsByTypeID(int typeID,int pageNumber,int pageSize) throws SQLException {
        if(typeID==0)
        {
            String sql="select * from goods limit ? , ?";
            QueryRunner r=new QueryRunner(DataSourceUtils.getDataSource());
            return  r.query(sql,new BeanListHandler<Goods>(Goods.class),(pageNumber-1)*pageSize,pageSize);
        }
        else
        {
            String sql="select * from goods where type_id=? limit ? , ?";
            QueryRunner r=new QueryRunner(DataSourceUtils.getDataSource());
            return  r.query(sql,new BeanListHandler<Goods>(Goods.class),typeID,(pageNumber-1)*pageSize,pageSize);
        }
    }
    public int getCountOfGoodsByTypeID(int typeID) throws SQLException {
        String sql="";
        QueryRunner r=new QueryRunner(DataSourceUtils.getDataSource());
        if(typeID==0)
        {
            sql="select count(*) from goods";
            return r.query(sql,new ScalarHandler<Long>()).intValue();
        }
        else
        {
            sql="select count(*) from goods where type_id=?";
            return r.query(sql,new ScalarHandler<Long>(),typeID).intValue();
        }
    }
    public int getMostOrderTypeByUserId(int user_id) throws SQLException {
        String sql="";
        QueryRunner r=new QueryRunner(DataSourceUtils.getDataSource());
        sql="SELECT type_id AS type_id FROM order_item\n" +
                "WHERE order_id IN (SELECT order_id FROM `orders` WHERE user_id = ?)\n" +
                "GROUP BY type_id\n" +
                "ORDER BY SUM(amount) DESC\n" +
                "LIMIT 1;";
        Integer typeId = r.query(sql, new ScalarHandler<Integer>(), user_id);
        return  typeId != null ? typeId : 0;
    }

    public int getMostVisitTypeByUserId(int user_id) throws SQLException {
        String sql="";
        QueryRunner r=new QueryRunner(DataSourceUtils.getDataSource());
        sql="SELECT type_id FROM user_visit\n" +
                "WHERE user_id = ?\n" +
                "GROUP BY type_id\n" +
                "ORDER BY SUM(visit_time) DESC\n" +
                "LIMIT 1;";
        Integer typeId = r.query(sql, new ScalarHandler<Integer>(), user_id);
        return  typeId != null ? typeId : 0;
    }

    public int getSellmanCountOfGoodsByTypeID(int type_id) throws SQLException {
        String sql="";
        QueryRunner r=new QueryRunner(DataSourceUtils.getDataSource());
        sql="select count(*) from goods where type_id=?";
        return r.query(sql,new ScalarHandler<Long>(),type_id).intValue();
    }
    public List<Goods> selectGoodsbyRecommend(int type,int pageNumber,int pageSize) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        if(type==0||type==4) {
            //当不添加推荐类型限制的时候
            String sql = " select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from goods g,type t where g.type_id=t.id order by g.id limit ?,?";
            return r.query(sql, new BeanListHandler<Goods>(Goods.class),(pageNumber-1)*pageSize,pageSize);

        }
        String sql = " select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from goods g,recommend r,type t where g.id=r.goods_id and g.type_id=t.id and r.type=? order by g.id limit ?,?";
        return r.query(sql, new BeanListHandler<Goods>(Goods.class),type,(pageNumber-1)*pageSize,pageSize);
    }

    public List<Goods> selectSellmanGoodsbyRecommend(int type,int pageNumber,int pageSize, int type_id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        if(type==0) {
            //当不添加推荐类型限制的时候
            String sql = " select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from goods g,type t where g.type_id=t.id and g.type_id = ? order by g.id limit ?,?";
            return r.query(sql, new BeanListHandler<Goods>(Goods.class), type_id,(pageNumber-1)*pageSize,pageSize);

        }
        String sql = " select g.id,g.name,g.cover,g.image1,g.image2,g.intro,g.price,g.stock,t.name typename from goods g,recommend r,type t where g.id=r.goods_id and g.type_id=t.id and r.type=? and g.type_id=? order by g.id limit ?,?";
        return r.query(sql, new BeanListHandler<Goods>(Goods.class),type,type_id,(pageNumber-1)*pageSize,pageSize);
    }
    public int getRecommendCountOfGoodsByTypeID(int type) throws SQLException {
        if(type==0||type==4)return getCountOfGoodsByTypeID(0);
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from recommend where type=?";
        return r.query(sql, new ScalarHandler<Long>(),type).intValue();
    }

    public int getSellmanRecommendCountOfGoodsByTypeID(int type, int type_id) throws SQLException {
        if(type==0)return getSellmanCountOfGoodsByTypeID(type_id);
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from recommend where type=? and type_id=?";
        return r.query(sql, new ScalarHandler<Long>(),type, type_id).intValue();
    }

    public Goods getGoodsById(int id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select g.id,g.name,g.cover,g.image1,g.image2,g.price,g.intro,g.stock,t.id typeid,t.name typename from goods g,type t where g.id = ? and g.type_id=t.id";
        return r.query(sql, new BeanHandler<Goods>(Goods.class),id);
    }
    public int getSearchCount(String keyword) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from goods where name like ?";
        return r.query(sql, new ScalarHandler<Long>(),"%"+keyword+"%").intValue();
    }
    public List<Goods> selectSearchGoods(String keyword, int pageNumber, int pageSize) throws SQLException{
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from goods where name like ? limit ?,?";
        return r.query(sql, new BeanListHandler<Goods>(Goods.class),"%"+keyword+"%",(pageNumber-1)*pageSize,pageSize);
    }
    public boolean isScroll(Goods g) throws SQLException {
        return isRecommend(g, 1);
    }
    public boolean isHot(Goods g) throws SQLException {
        return isRecommend(g, 2);
    }
    public boolean isNew(Goods g) throws SQLException {
        return isRecommend(g, 3);
    }
    private boolean isRecommend(Goods g,int type) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from recommend where type=? and goods_id=?";
        Recommend recommend = r.query(sql, new BeanHandler<Recommend>(Recommend.class),type,g.getId());
        if(recommend==null) {
            return false;
        }else {
            return true;
        }
    }
    public void addRecommend(int id,int type,int type_id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into recommend(type,goods_id, type_id) values(?,?,?)";
        r.update(sql,type,id,type_id);
    }
    public void removeRecommend(int id,int type) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "delete from recommend where type=? and goods_id=?";
        r.update(sql,type,id);
    }
    public void insert(Goods g) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into goods(name,cover,image1,image2,price,intro,stock,type_id) values(?,?,?,?,?,?,?,?)";
        r.update(sql,g.getName(),g.getCover(),g.getImage1(),g.getImage2(),g.getPrice(),g.getIntro(),g.getStock(),g.getType().getId());
    }
    public void update(Goods g) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update goods set name=?,cover=?,image1=?,image2=?,price=?,intro=?,stock=?,type_id=? where id=?";
        r.update(sql,g.getName(),g.getCover(),g.getImage1(),g.getImage2(),g.getPrice(),g.getIntro(),g.getStock(),g.getType().getId(),g.getId());
    }

    public void updateRecommend(int type_id, int goods_id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update recommend set type_id=? where goods_id=?";
        r.update(sql,type_id, goods_id);
    }

    public void delete(int id) throws SQLException {
        QueryRunner r = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "delete from goods where id = ?";
        r.update(sql,id);
    }


    public int sumAmountById(Connection con, int Id) throws SQLException {
        int totalAmount = 0;
        String sql = "SELECT amount FROM order_item WHERE goods_id = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, Id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalAmount += resultSet.getDouble("amount");
                }
            }
        }
        return totalAmount;
    }


}
