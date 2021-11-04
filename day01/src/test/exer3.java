package test;

import PrepareStatementCrud2.bean.Student;
import PrepareStatementCrud2.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @author CJYong
 * @create 2021-07-10 15:25
 */
public class exer3 {
    @Test
    public void LookInfo(){
        boolean isFlag = true;
        while (isFlag){
            System.out.println("请选择您要输入的类型：");
            System.out.println("a：准考证号");
            System.out.println("b：身份证号");
            System.out.println("c：删除学生信息");

            Scanner scan = new Scanner(System.in);
            String choice = scan.nextLine();
            switch (choice){
                case "a":
                    System.out.println("请输入准考证号：");
                    String examCard = scan.nextLine();
                    String sql1 = "select FlowID FlowId,Type,IDCard,ExamCard,StudentName,Location,Grade from examstudent where ExamCard  = ? ";
                    try {
                        Student student = geInstance(Student.class, sql1, examCard);

                        System.out.println("————————查询结果————————————");
                        System.out.println("流水号：" + student.getFlowId());
                        System.out.println("四级/六级：" + student.getType());
                        System.out.println("身份证号：" + student.getIDCard());
                        System.out.println("准考证号：" + student.getExamCard());
                        System.out.println("学生姓名：" + student.getStudentName());
                        System.out.println("区域：" + student.getLocation());
                        System.out.println("成绩：" + student.getGrade());
                    } catch (NullPointerException e) {
                        System.out.println("查无此人，请重新进入程序 ！");
                    }
                    break;
                case "b":
                    System.out.println("请输入身份证号：");
                    String idCard = scan.nextLine();
                    String sql2 = "select FlowID FlowId,Type,IDCard,ExamCard,StudentName,Location,Grade from examstudent where IDCard = ? ";
                    try {
                        Student student1 = geInstance(Student.class, sql2, idCard);
                        System.out.println("————————查询结果————————————");
                        System.out.println("流水号：" + student1.getFlowId());
                        System.out.println("四级/六级：" + student1.getType());
                        System.out.println("身份证号：" + student1.getIDCard());
                        System.out.println("准考证号：" + student1.getExamCard());
                        System.out.println("学生姓名：" + student1.getStudentName());
                        System.out.println("区域：" + student1.getLocation());
                        System.out.println("成绩：" + student1.getGrade());
                    } catch (Exception e) {
                        System.out.println("查无此人,请重新进入程序");
                    }
                    break;
                case "c":
                    System.out.println("请输入学生的考号：");
                    String exCard = scan.nextLine();

                    String sql4 = "delete from examstudent where ExamCard = ?";
                    int deleteCount = upDate(sql4, exCard);
                    if (deleteCount > 0){
                        System.out.println("删除成功！");
                    }else {
                        System.out.println("查无此人，请重新输入 ！");
                    }

                default:
                    System.out.println("您的输入有误！ 请重新选择进入程序");
            }
        }
    }

    /**
     * 针对不同的表的通用的查询操作，返回表中的一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public<T> T geInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //给t对象指定的columnName属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResourse(conn,ps,rs);
        }

        return null;
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