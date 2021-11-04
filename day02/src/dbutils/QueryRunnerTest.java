package dbutils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;
import transaction.DAO.bean.Customer;
import transaction.util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，
 * 它是对JDBC的简单封装，学习成本极低，并且使用dbutils能极大简化jdbc编码的工作量，
 * 同时也不会影响程序的性能。
 * @author CJYong
 * @create 2021-07-12 19:41
 */
public class QueryRunnerTest {
    @Test
    /**
     * 测试 增 删 改 操作
     */
    public void testInsert()  {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?) ";
            int insertCount = runner.update(conn, sql, "张天择", "zhangtianze@163.com", "1990-07-25");
            System.out.println("添加了" + insertCount + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tools.JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    /**
     * 查询测试
     * BeanHandler： 是ResultSetHandler接口的实现类，用于封装表中的一条记录。
     */
    public void testQuery1()  {
        Connection conn = null;
        try {
            //创建工具类对象
            QueryRunner runner = new QueryRunner();
            //获取数据库连接
            conn = JDBCUtils.getConnection3();
            //编写SQL语句
            String sql = "select id,name,birth from customers where id = ?";
            //封装对象
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 21);
            //输出结果
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

    @Test
    /**
     * BeanListHandler : 是ResultSetHandler接口的实现类，用于封装表中的多条记录
     */
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,birth from customers where id < ?";
            //封装对象
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler, 21);
            //输出结果
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

    @Test
    /**
     * MapHandler : 是ResultSetHandler接口的实现类，用于封装表中的一条记录
     * 将字段及相应的字段值作为map中的 key 和 value
     */
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,birth from customers where id = ?";
            //封装对象
            MapHandler handler = new MapHandler();

            Map<String, Object> map = runner.query(conn, sql, handler, 5);
            //输出结果
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

    @Test
    /**
     * MapListHandler : 是ResultSetHandler接口的实现类，用于封装表中的多条记录
     * 将字段及相应的字段值作为map中的 key 和 value, 并将这些 map 添加到 list 当中
     */
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,birth from customers where id  < ?";
            //封装对象
            MapListHandler handler = new MapListHandler();

            List<Map<String, Object>> list = runner.query(conn, sql, handler, 21);
            //输出结果
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

    /**
     * 特殊值的查询:  ScalarHandler
     */
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select count(*) from customers";
            //封装对象
            ScalarHandler handler = new ScalarHandler();

            long count = (long) runner.query(conn, sql, handler);
            //输出结果
            System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

    @Test
    public void testQuery6() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select max(birth) from customers";
            //封装对象
            ScalarHandler handler = new ScalarHandler();

            java.sql.Date maxBirth = (Date) runner.query(conn, sql, handler);
            //输出结果
            System.out.println(maxBirth);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

    @Test
    /**
     * 自定义ResultSetHandler的实现类
     */
    public void testQuery7() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?";
            //封装对象
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>(){

                @Override
                public Customer handle(ResultSet rs) throws SQLException {
//                    return null;
                    return new Customer(12,"唐嫣","tangyan@qq.com",new java.util.Date(454123132));
                }
            };

            Customer customer = runner.query(conn, sql, handler, 23);
            //输出结果
            System.out.println(customer);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            tools.JDBCUtils.closeResourse(conn, null);
        }

    }

}
