package Blob;

import PrepareStatementCrud2.bean.Customer;
import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**使用preparedStatement操作Blob类型的数据
 * @author CJYong
 * @create 2021-07-11 10:31
 */
public class BlobTest {
    @Test
    //向数据表中插入blob类型的数据
    public void testInsert() throws  Exception{
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"吴亦凡");
        ps.setObject(2,"wuyifan@qq.com");
        ps.setObject(3,"1989-07-15");
        FileInputStream is = new FileInputStream((new File("ORM.png")));
        ps.setBlob(4, is);

        ps.execute();

        JDBCUtils.closeResourse(conn, ps);


    }

    //查询数据表中blob类型的数据
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1,22);

            rs = ps.executeQuery();
            if (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date birth = rs.getDate(4);

                Customer cust = new Customer(id, name, email, birth);
                System.out.println(cust);

                //将Blob类型的字段下载下来保存到本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("ORM_Test.png");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResourse(conn,ps,rs);
        }

    }


}
