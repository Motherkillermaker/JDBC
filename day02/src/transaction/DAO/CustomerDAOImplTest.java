package transaction.DAO;

import org.junit.Test;
import tools.JDBCUtils;
import transaction.DAO.bean.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author CJYong
 * @create 2021-07-12 15:29
 */
public class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();


    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer cust = new Customer(1,"梅西","meixi@163.com",new Date(22132132113l));
            dao.insert(conn,cust);
            System.out.println("添加成功 ！");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    public void delectById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();

            dao.delectById(conn,12);

            System.out.println("删除成功 ！");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer cust = new Customer(23,"C罗","cluo@126.com",new Date(544143213));
            dao.update(conn,cust);

            System.out.println("修改成功 ！");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    public void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer cust = dao.getCustomerById(conn, 16);
            System.out.println(cust);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    public void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Customer> list = dao.getAll(conn);
            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            long count = dao.getCount(conn);

            System.out.println("表中的记录数为：" + count);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            java.sql.Date maxBirth = dao.getMaxBirth(conn);

            System.out.println("最大生日为：" + maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,null);
        }
    }
}