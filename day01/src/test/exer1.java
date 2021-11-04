package test;

import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @author CJYong
 * @create 2021-07-10 14:15
 */
public class exer1 {

    @Test
    public void testInsert() {
        Scanner scan = new Scanner(System.in);

        System.out.println("name:");
        String custName = scan.nextLine();
        System.out.println("email:");
        String custEmail = scan.nextLine();
        System.out.println("birth:");
        String custBirth = scan.nextLine();

        String sql = "insert into customers set name = ?, email = ?,birth = ?";
        int insertCount = upDate(sql, custName, custEmail, custBirth);
        if (insertCount > 0){
            System.out.println("添加成功");
        }else {
            System.out.println("添加失败");
        }
    }

    //通用的增删改操作
    public int upDate(String sql,Object ...args)  { // sql中占位符的个数与可变形参的长度一致
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
            //4.执行: 执行查询操作有返回值返回true，执行增删改操作没有返回结果返回false
//            ps.execute();                                      方式一
           return  ps.executeUpdate();  //返回操作数据的行数       方式二
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResourse(conn,ps);

        }
        return 0;
    }

}





