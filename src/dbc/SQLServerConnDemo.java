package dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class SQLServerConnDemo {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SQLServerConnDemo conn = new SQLServerConnDemo();
		conn.connect();
	}
	
	/**
	 * 
	 */
	public void connect() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String URL = "jdbc:odbc:sqlserver_student";
			// 连接
			Connection conn;
			conn = DriverManager.getConnection(URL,"sa","lmj606");
			// 生成statement
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "select * from studentinfo";
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
					if(temp==null)
						temp="";
					System.out.print(temp+space(20-temp.length()));
				}
				System.out.println();
			}
			
			rs.first();
			System.out.println(rs.getString("sName"));

			rs.close();
			stmt.close();
			
			//用PreparedStatement，可以后来设定参数
			PreparedStatement pstmt = conn.prepareStatement("select * from studentinfo WHERE sNo = ?");
			pstmt.setString(1, "ST1001");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// 输出信息
				for (int i = 1; i <= n; i++){
					temp=rs.getString(i);
					if(temp==null)
						temp="";
					System.out.print(temp+space(20-temp.length()));
				}
				System.out.println();
			}
			rs.close();
			pstmt.close();
			
			conn.close();
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.toString());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.toString());
		}
	}
	
	/**
	 * 构造n空格的string
	 * @param n int 个数
	 * @return n个空格的string
	 */
	private String space(int n){
		String space="";
		for(int i=0;i<n;i++)
			space+=" ";
		return space;
	}
}
