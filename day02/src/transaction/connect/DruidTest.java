package transaction.connect;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author CJYong
 * @create 2021-07-12 19:17
 */
public class DruidTest {

    @Test
    /**
     * 获取Druid数据库连接池 ，开发中常用 ！     （重点）                 推荐方式
     */
   public void getConnection3() throws Exception{
        /**
         * 1.加载对应连接池的驱动
         * 2.创建对应连接数据库的配置文件
         * 3.创建数据库连接池
         * 4.获取连接池中的一个连接
         */

       Properties pros = new Properties();
       InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
       pros.load(is);

       //创建数据库连接池
       DataSource source = DruidDataSourceFactory.createDataSource(pros);

       Connection conn = source.getConnection();

       System.out.println(conn);
   }
}
