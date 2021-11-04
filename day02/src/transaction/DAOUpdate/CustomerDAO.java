package transaction.DAOUpdate;

import transaction.DAO.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**此接口用于针对customer表的常用操作
 * @author CJYong
 * @create 2021-07-12 14:21
 */
public interface CustomerDAO {
    /**
     * 将cust对象添加到数据库中
     * @param conn
     * @param cust
     */
    void insert(Connection conn, Customer cust);

    /**
     * 针对指定的id，删除表中的一条记录
     * @param conn
     * @param id
     */
    void delectById(Connection conn,int id);

    /**
     *针对内存中的cust对象，去修改数据表中指定的记录
     * @param conn
     * @param cust
     */
    void update(Connection conn,Customer cust);

    /**
     * 针对指定的id查询对应的customer对象
     * @param conn
     * @param id
     */
    Customer getCustomerById(Connection conn,int id);

    /**
     * 查询表中所有记录构成的集合
     * @param conn
     * @return
     */
    List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中数据的条目数
     * @param conn
     * @return
     */
    long getCount(Connection conn);

    /**
     * 返回数据表中最大的生日
     * @param conn
     * @return
     */
    Date getMaxBirth(Connection conn);



}
