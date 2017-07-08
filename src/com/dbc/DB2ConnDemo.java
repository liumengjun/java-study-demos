package com.dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DB2ConnDemo {

    public void connect() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String URL = "jdbc:odbc:db2test";
            // 连接
            Connection conn;
            conn = DriverManager.getConnection(URL);
            // 生成statement
            Statement stmt = conn.createStatement();
            String sql = "select * from rankschool";
            // 执行
            ResultSet rs = stmt.executeQuery(sql);
            //元数据(meta-data)对象
            ResultSetMetaData rsmd=rs.getMetaData();
            int n=rsmd.getColumnCount();
            String temp;
            
            for (int i = 1; i <= n; i++){
                temp=rsmd.getColumnLabel(i)+"("+rsmd.getColumnTypeName(i)+")";
                System.out.print(temp+space(20-temp.length()));
            }
            System.out.println();
            
            while (rs.next()) {
                // 输出信息
                for (int i = 1; i <= n; i++){
                    temp=rs.getString(i);
                    System.out.print(temp+space(20-temp.length()));
                }
                System.out.println();
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "连接数据库失败,请稍后再试。");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "连接数据失败,请稍后再试。");
        }
    }

    private String space(int n){//构造n空格的string
        String space="";
        for(int i=0;i<n;i++)
            space+=" ";
        return space;
    }
    public static void main(String[] args) {
        DB2ConnDemo conn = new DB2ConnDemo();
        conn.connect();
    }
}
