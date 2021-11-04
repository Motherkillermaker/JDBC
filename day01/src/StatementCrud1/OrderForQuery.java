package StatementCrud1;

import PrepareStatementCrud2.bean.Order;
import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**针对于order表的通用操作
 * @author CJYong
 * @create 2021-07-09 16:20
 */
public class OrderForQuery {

    @Test
    public void testOrderForQuery(){
        /**
         * SQL中的字段名和类的属性名不一致时，通过给表的字段名取别名来解决
         * 使用ResultSetMetaData时，使用getColumnLabel()来替换getColumnName()
         * 若SQL中没有给字段取别名，则getColumnLabel()方法获取的就是字段名
         */
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = orderForQuery(sql,1);
        System.out.println(order);

    }

    /**
     * 通用的针对于order表的查询操作
     */
    public Order orderForQuery(String sql,Object ...args){

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行并获取结果
            rs = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值: 通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列值：通过ResultSetMetaData
                    //获取列的列名：getColumnName()
                    //获取列的别名：getColumnLabel()
//                  String columnName = rsmd.getColumnName(i + 1);  不推荐使用
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn, ps, rs);
        }
        return null;
    }



    @Test
    public void testQuery1() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setObject(1, 1);

            rs = ps.executeQuery();
            if (rs.next()) {
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date) rs.getObject(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(conn, ps, rs);
        }
    }
}





