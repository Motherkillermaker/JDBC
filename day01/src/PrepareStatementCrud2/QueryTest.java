package PrepareStatementCrud2;

import PrepareStatementCrud2.bean.Customer;
import PrepareStatementCrud2.bean.Order;
import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**使用PrepareStatement来实现针对不同表的通用的查询操作
 * @author CJYong
 * @create 2021-07-10 13:02
 */
public class QueryTest {

    @Test
    public void testGetForList(){
//        String sql = "select id,name,email from customers where id < ?";
//        List<Customer> list = getForList(Customer.class, sql, 12);
//        list.forEach(System.out::println);

        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id < ?";
        List<Order> list = getForList(Order.class, sql, 5);
        list.forEach(System.out::println);
    }

    public<T> List<T> getForList(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合来接受返回的对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()){
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //给t对象指定的columnName属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps,rs);
        }

        return null;
    }


    @Test
    public void testGetInstance(){
//        String sql = "select id,name,email from customers where id = ?";
//        Object o = geInstance(Customer.class, sql, 12);
//        System.out.println(o);

        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Object o1 = geInstance(Order.class, sql, 4);
        System.out.println(o1);
    }

    /**
     * 针对不同的表的通用的查询操作，返回表中的一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public<T> T geInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //给t对象指定的columnName属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps,rs);
        }

        return null;
    }
}
