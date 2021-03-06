package StatementCrud1;

import PrepareStatementCrud2.bean.Customer;
import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.sql.*;

/**针对于customers表的查询操作
 * @author CJYong
 * @create 2021-07-09 15:25
 */
public class CustomersQuery {

    @Test
    public void testqueryForCustomers(){
//        String sql = "select id,name,birth,email from customers where id = ?";
        String sql1 = "select id,name,email from customers where name = ?";
        Customer customer = queryForCustomers(sql1, "周杰伦");
        System.out.println(customer);
    }




    /**
     * 针对customers表的通用查询操作
     */
    public Customer queryForCustomers(String sql,Object ...args)  {
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
                Customer cust = new Customer();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //给cust对象指定的columnName属性赋值为columnValue
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust,columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps,rs);
        }

        return null;

    }








    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);

            //执行,并返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            if (resultSet.next()){ // 判断结果集的下一条是否有数据，如果有返回true，并指针下移，如果返回false，指针不会下移
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

    //            //方式一:
    //            System.out.println("id = " + id + ",name = " + name + "email = " + email + ",birth = " + birth);
    //
    //            //方式二：
    //            Object[] data = new Object[]{id,name, email,birth};

                //方式三：将数据封装为一个对象(推荐)
                Customer customer = new Customer(id,name,email,birth);
                System.out.println(customer);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtils.closeResourse(conn, ps, resultSet);
        }
    }
}



