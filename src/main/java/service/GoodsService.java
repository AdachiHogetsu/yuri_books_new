package service;

import dao.GoodsDao;
import dao.OrderDao;
import model.Goods;
import model.Page;
import utils.DataSourceUtils;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GoodsService {
    private GoodsDao gDao=new GoodsDao();
    private OrderDao oDao = new OrderDao();
    public List<Map<String,Object>> getGoodsList(int recommendType) {
        List<Map<String,Object>> list=null;
        try {
            list=gDao.getGoodsList(recommendType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String,Object>> getScrollGood() {
        List<Map<String,Object>> list=null;
        try {
            list=gDao.getScrollGood();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Goods> selectGoodsByTypeID(int typeID, int pageNumber, int pageSize)
    {
        List<Goods> list=null;
        try {
            list=gDao.selectGoodsByTypeID(typeID,pageNumber,pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Page selectPageByTypeID(int typeID,int pageNumber)
    {
        Page p=new Page();
        p.setPageNumber(pageNumber);
        int totalCount=0;
        try {
            totalCount=gDao.getCountOfGoodsByTypeID(typeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(8,totalCount);

        List list=null;
        try {
            list=gDao.selectGoodsByTypeID(typeID,pageNumber,8);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        p.setList(list);
        return p;
    }
    public Page getGoodsRecommendPage(int type,int pageNumber) {
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int totalCount = 0;
        try {
            totalCount = gDao.getRecommendCountOfGoodsByTypeID(type);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(8, totalCount);
        List list=null;
        try {
            list = gDao.selectGoodsbyRecommend(type, pageNumber, 8);
            for(Goods g : (List<Goods>)list) {
                g.setScroll(gDao.isScroll(g));
                g.setHot(gDao.isHot(g));
                g.setNew(gDao.isNew(g));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setList(list);
        return p;
    }

    public Page getSelfRecommendPage(int pageNumber, int user_id) {
        Page p=new Page();
        p.setPageNumber(pageNumber);
        int totalCount=0;
        int order_most_type_id = 0, visit_most_type_id= 0;
        try {
            order_most_type_id =gDao.getMostOrderTypeByUserId(user_id);
            visit_most_type_id =gDao.getMostVisitTypeByUserId(user_id);
            //System.out.println(order_most_type_id);
            //System.out.println(visit_most_type_id);
            if(order_most_type_id==visit_most_type_id){
                totalCount=gDao.getCountOfGoodsByTypeID(order_most_type_id);
            }
            else if(order_most_type_id==0&&visit_most_type_id==0){
                totalCount=gDao.getCountOfGoodsByTypeID(0);
            }
            else if(order_most_type_id==0&&visit_most_type_id!=0){
                totalCount=gDao.getCountOfGoodsByTypeID(visit_most_type_id);
            }
            else if(order_most_type_id!=0&&visit_most_type_id==0){
                totalCount=gDao.getCountOfGoodsByTypeID(order_most_type_id);
            }
            else{
                totalCount+=gDao.getCountOfGoodsByTypeID(order_most_type_id);
                totalCount+=gDao.getCountOfGoodsByTypeID(visit_most_type_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(8,totalCount);

        List list=null;
        try {
            if(order_most_type_id==visit_most_type_id){
                list=gDao.selectGoodsByTypeID(order_most_type_id,pageNumber,8);
            }
            else if(order_most_type_id==0&&visit_most_type_id==0){
                list=gDao.selectGoodsByTypeID(0,pageNumber,8);
            }
            else if(order_most_type_id==0&&visit_most_type_id!=0){
                list=gDao.selectGoodsByTypeID(visit_most_type_id,pageNumber,8);
            }
            else if(order_most_type_id!=0&&visit_most_type_id==0){
                list=gDao.selectGoodsByTypeID(order_most_type_id,pageNumber,8);
            }
            else{
                list=gDao.selectGoodsByTypeID(order_most_type_id,pageNumber,8);
                list.addAll(gDao.selectGoodsByTypeID(visit_most_type_id,pageNumber,8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        p.setList(list);
        return p;
    }

    public Page getSellmanGoodsRecommendPage(int type,int pageNumber, int type_id) {
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int totalCount = 0;
        try {
            totalCount = gDao.getSellmanRecommendCountOfGoodsByTypeID(type,type_id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(8, totalCount);
        List list=null;
        try {
            list = gDao.selectSellmanGoodsbyRecommend(type, pageNumber, 8,type_id);
            for(Goods g : (List<Goods>)list) {
                g.setScroll(gDao.isScroll(g));
                g.setHot(gDao.isHot(g));
                g.setNew(gDao.isNew(g));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setList(list);
        return p;
    }
    public Goods getGoodsById(int id) {
        Goods g=null;
        try {
            g = gDao.getGoodsById(id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return g;
    }
    public Page getSearchGoodsPage(String keyword, int pageNumber) {
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int totalCount = 0;
        try {
//			totalCount = gDao.getGoodsCount(typeId);
            totalCount = gDao.getSearchCount(keyword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(8, totalCount);
        List list=null;
        try {
//			list = gDao.selectGoods(keyword, pageNo, 8);
            list = gDao.selectSearchGoods(keyword,pageNumber,8);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setList(list);
        return p;
    }
    public void addRecommend(int id,int type, int type_id) {
        try {
            gDao.addRecommend(id, type, type_id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void removeRecommend(int id,int type) {
        try {
            gDao.removeRecommend(id, type);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void insert(Goods goods) {
        try {
            gDao.insert(goods);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void update(Goods goods) {
        try {
            gDao.update(goods);
            gDao.updateRecommend(goods.getType().getId(),goods.getId());
            oDao.updateTypeid(goods.getId(),goods.getType().getId());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public boolean delete(int id) {
        try {
            gDao.delete(id);
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }



    public int calculateAmount(int g_id){
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection();
            return gDao.sumAmountById(con,g_id);
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
