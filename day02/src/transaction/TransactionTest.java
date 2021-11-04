package transaction;

import org.junit.Test;
import tools.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author CJYong
 * @create 2021-07-11 15:38
 */
public class TransactionTest {

    @Test
    /*************未考虑数据库事务情况下的转账操作
     * 针对于数据表user_table来说，AA用户给BB用户转账100
     * update user_table set balance = balance - 100 where user = 'AA';
     * update user_table set balance = balance + 100 where user = 'BB';
     */
    public void test1(){
        String sql1 = "update user_table set balance = balance - 100 where user = ? ";
        upDate(sql1,"AA");

        //模拟网络异常
        System.out.println(10/0);

        String sql2 = "update user_table set balance = balance + 100 where user = ? ";
        upDate(sql2,"BB");

        System.out.println("转账成功 ！");

    }

    //通用的增删改操作 - version 1.0
    public int upDate(String sql, Object... args) { // sql中占位符的个数与可变形参的长度一致
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回preparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);  //sql下标从1开始，java下标从0开始

            }
            //4.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResourse(conn, ps);

        }
        return 0;
    }


    //*************考虑数据库事务的转账操作**************************
    @Test
    public void test2() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            //1.取消数据的自动提交功能
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ? ";
            upDate(conn,sql1,"AA");

            //模拟网络异常
            System.out.println(10/0);

            String sql2 = "update user_table set balance = balance + 100 where user = ? ";
            upDate(conn,sql2,"BB");

            System.out.println("转账成功 ！");

            //2.提交数据
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            //3.回滚操作
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }finally {
            JDBCUtils.closeResourse(conn,null);
        }

    }


    //通用的增删改操作 - version 2.0   考虑事务
    public int upDate(Connection conn,String sql, Object... args) { // sql中占位符的个数与可变形参的长度一致
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回preparedStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);  //sql下标从1开始，java下标从0开始

            }
            //3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.资源的关闭

            //修改其为自动提交数据
            //主要针对于使用数据库连接池的使用
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            JDBCUtils.closeResourse(null, ps);

        }
        return 0;
    }

    //*****************************************************************************
    @Test
    public void testTransactionSelect() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //设置数据库的连接级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";
        User user = geInstance(conn, User.class, sql, "CC");
        System.out.println(user);

    }

    @Test
    public void testTransactionUpdate() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        //取消自动提交
        conn.setAutoCommit(false);

        String sql = "update user_table set balance = ? where user = ?";
        upDate(conn,sql,5000,"CC");

        Thread.sleep(15000);
        System.out.println("修改结束");
    }



    /**
     * 针对不同的表的通用的查询操作，返回表中的一条记录(version 2.0  - 考虑上事务)
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public<T> T geInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
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
            JDBCUtils.closeResourse(null,ps,rs);
        }

        return null;
    }
}
