import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author CJYong
 * @create 2021-07-08 15:12
 */
public class ConnectionTest {

    //方式一
    @Test
    public void test1() throws SQLException {
        //获取driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        //url
        //jdbc:         mysql 协议
        //localhost:    ip地址
        //3306：        默认mysql端口号
        //test：        test数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //将用户名和密码封装到properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","203731");
        Connection conn = driver.connect(url, info);

        System.out.println(conn);

    }

    //方式二: 利用反射来创建driver的实现类
    @Test
    public void test2() throws Exception {
        //1.获取driver的实现类对象
        Class clszz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clszz.newInstance();

        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //3.提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","203731");

        //4.获取连接
        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }
    //方式三： 使用DriverManger 替换Driver
    @Test
    public void test3() throws Exception{
        //1.获取driver实现类的对象
        Class clszz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clszz.newInstance();

        //2.提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "203731";

        //3.注册驱动
        DriverManager.registerDriver(driver);

        //4.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);

    }
    //方式四：
    @Test
    public void test4() throws Exception{
        //1.提供三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "203731";

        //2.加载driver
        Class.forName("com.mysql.jdbc.Driver");
        //省略驱动的注册 （类的静态代码块中执行驱动的注册）
//        Driver driver = (Driver) clszz.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);

        //3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);

    }

    //方式五(最终版): 将数据库需要的4个基本信息声明在配置文件中，通过读取配置文件的方式获取连接
    //实现数据和代码的分离 （解耦）,如果需要修改配置文件信息可以避免重新打包
    @Test
    public void test5() throws Exception {
        //1.读取配置文件中的四个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection conn = DriverManager.getConnection(url,user,password);

        System.out.println(conn);
    }
}
