package Blob;

import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**使用preparedstatement实现批量数据的操作
 * update、delete本身具有批量操作的效果
 * 批量操作主要指的是批量插入(insert),使用preparedstatement实现更高效的插入
 *
 * @author CJYong
 * @create 2021-07-11 12:48
 */

public class InsertTest {
    /*
    方式四：向goods表中插入两万条数据（使用preparedstatement）
    设置不允许自动提交
    */
    @Test
    public void test4(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtils.getConnection();

            //设置不允许自动提交
            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i <= 1000000; i++) {
                ps.setObject(1,"name_" + i);

                //1."攒"sql
                ps.addBatch();

                if (i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();

                    //3.清空batch
                    ps.executeBatch();
                }

            }

            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();
            System.out.println("time = " + (end - start));  //2w条数据：time = 428
                                                            //100w条数据：time = 7929
                                                            //方式四：time = 4965

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps);
        }
    }


    @Test
    /*
    方式三：向goods表中插入两万条数据（使用preparedstatement）
    1.addBatch(),excuteBatch(),clearBatch();
    2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
 	?rewriteBatchedStatements=true 写在配置文件的url后面
 	3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
    */
    public void test3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i <= 1000000; i++) {
                ps.setObject(1,"name_" + i);

                //1."攒"sql
                ps.addBatch();

                if (i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();

                    //3.清空batch
                    ps.executeBatch();
                }

            }

            long end = System.currentTimeMillis();
            System.out.println("time = " + (end - start));  //2w条数据：time = 428
                                                            //100w条数据：time = 7929

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps);
        }
    }


    @Test
    /*
    方式二：向goods表中插入两万条数据（使用preparedstatement）
    */
    public void test2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i <= 20000; i++) {
                ps.setObject(1,"name_" + i);

                ps.execute();

            }

            long end = System.currentTimeMillis();
            System.out.println("time = " + (end - start));  //time = 23778

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps);
        }

    }


    @Test
    /*
    方式一：向goods表中插入两万条数据（使用statement）
    */
    public void test1() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        Statement st = conn.createStatement();
        for (int i = 0; i <= 2000; i++) {
            String sql = "insert into goods (name)values('name_  "+ i +" ')";
            st.execute(sql);
        }

    }
}
