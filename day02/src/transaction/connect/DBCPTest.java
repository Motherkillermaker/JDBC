package transaction.connect;

import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author CJYong
 * @create 2021-07-12 18:41
 */
public class DBCPTest {

    @Test
    /**方式一：不推荐
     * 测试DBCP的数据库连接池技术
     */
    public void testGetConnection() throws SQLException {
        //创建DBCP的数据库连接池
        BasicDataSource sources = new BasicDataSource();

        //设置基本信息
        sources.setDriverClassName("com.mysql.jdbc.Driver");
        sources.setUrl("jdbc:mysql:///test");
        sources.setUsername("root");
        sources.setPassword("203731");

        //设置其他数据库管理的其他属性
        sources.setInitialSize(10);
        sources.setMaxActive(10);

        Connection conn = sources.getConnection();
        System.out.println(conn);

    }

    @Test
    /**
     * 使用配置文件來使用dbcp数据库连接池 （推荐使用）
     */
    public void getConnection2() throws Exception {
        Properties pros = new Properties();
        //方式一：
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二：
        InputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();

        System.out.println(conn);
    }
}
