package PrepareStatementCrud2;

import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author CJYong
 * @create 2021-07-09 13:06
 */

/*使用PreparedStatement来替换Statement,实现对数据表的增删改查操作

    增删改

    查
 */

public class UpdateTest {

    //通用的增删改操作
    @Test
    public void test(){
        //增加操作
//        String sql = "insert into `order` set order_id = ? ,order_name = ? , order_date = ?";
//        upDate(sql,"3","CC",null);

        //删除操作
//        String sql = "delete from `order` where order_id = ?";
//        upDate(sql,"3");

        //修改操作
         String sql = "update `order` set order_name = ? where order_id = ?";
         upDate(sql,"CC","2");
    }



    public void upDate(String sql,Object ...args)  { // sql中占位符的个数与可变形参的长度一致
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回preparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);  //sql下标从1开始，java下标从0开始

            }
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResourse(conn,ps);

        }
    }




    //修改customer表的一条记录
    @Test
    public void testUpdate()  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回preparedStatement的实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setString(1,"王祖贤");
            ps.setObject(2,"20");
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResourse(conn,ps);
        }
    }


    //向customer表中添加一条记录
    @Test
    public void testInsert()  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.读取配置文件中的四个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            //2.加载驱动
            Class.forName(driverClass);

            //3.获取连接
            conn = DriverManager.getConnection(url, user, password);

//        System.out.println(conn);

            //4.预编译sql语句，返回preparedStatement的实例
            String sql = "insert into customers(name,email,birth)value (?,?,?)"; // ? : 占位符
            ps = conn.prepareStatement(sql);

            //5.填充占位符 (数据库索引从 1 开始)
            ps.setString(1,"王祖蓝");
            ps.setString(2,"wangzulan@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1987-01-01");
            ps.setDate(3,new java.sql.Date(date.getTime()));

            //6.执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //7.资源的关闭
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }



    }

}
