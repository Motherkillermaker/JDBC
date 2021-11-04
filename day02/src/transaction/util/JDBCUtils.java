package transaction.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import transaction.connect.DBCPTest;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author CJYong
 * @create 2021-07-12 18:33
 */
public class JDBCUtils {
    //使用C3P0数据库连接池的配置文件方式，获取数据库的连接：推荐
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection1() throws SQLException{
        Connection conn = cpds.getConnection();
        return conn;
    }



    //使用dbcp数据库连接池的配置文件方式，获取数据库的连接：推荐
    public static Connection getConnection2() throws Exception {
        Properties pros = new Properties();
        //方式一：
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二：
        InputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        //创建一个dbcp数据库连接池
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();

        return conn;
    }

    //最终版
    private static DataSource source = null;
    static{
        try {
            Properties pros = new Properties();

            InputStream is = DBCPTest.class.getClassLoader().getResourceAsStream("dbcp.properties");

            pros.load(is);
            //根据提供的BasicDataSourceFactory创建对应的DataSource对象
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection4() throws Exception {

        Connection conn = source.getConnection();

        return conn;
    }

    //使用druid数据库连接池的配置文件方式，获取数据库的连接：推荐
    public static Connection getConnection3() throws Exception{
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);

        //创建数据库连接池
        DataSource source = DruidDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();

        return conn;
    }
}
