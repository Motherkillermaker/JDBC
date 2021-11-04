package transaction;

import org.junit.Test;
import tools.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author CJYong
 * @create 2021-07-11 15:34
 */
public class ConnectionTest {
    @Test
    public void test1() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
