package dbc;

import java.sql.*;
import javax.swing.JOptionPane;

//import oracle.jdbc.pool.OracleDataSource;

/**
 * 
 * 参考 "[ORACLE_HOME]\jdbc\Readme.txt" 文件
 * 
 * On Windows platforms:
  - Add [ORACLE_HOME]\jdbc\lib\classes12.jar to your CLASSPATH if you
    use JDK 1.2 or 1.3.  Add [ORACLE_HOME]\jdbc\lib\ojdbc14.jar to
    your CLASSPATH if you use JDK 1.4.
  - Add [ORACLE_HOME]\jlib\orai18n.jar to your CLASSPATH if needed.
  - Add [ORACLE_HOME]\bin to your PATH if you are using the JDBC OCI
    driver.
 */
public class OracleJDBCDemo {

	public static void main(String[] args) {
		try {
			// 连接
			Connection conn;
			String URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
			String user = "scott",password = "tiger";
			/*方法1：Traditional Method
			 */
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(URL,user,password);
			/*
			 方法2：Create an OracleDataSource instance. 
			 To use OracleDataSource, you need to do:
				import oracle.jdbc.pool.OracleDataSource;
			OracleDataSource ods = new OracleDataSource();
			ods.setUser(user);
		    ods.setPassword(password);
		    ods.setURL(URL);
		    conn = ods.getConnection();
			 */
		    
			// 生成statement
			Statement stmt = conn.createStatement();
			String sql = "select * from emp";
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
					if(temp==null){
						temp="null";
					}
					System.out.print(temp+space(20-temp.length()));
				}
				System.out.println();
			}

			rs.close();
			stmt.close();
			conn.close();
		} /*只有在方法1时 */
		catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "连接数据库失败,请稍后再试。");
			ex.printStackTrace();
		}catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "连接数据失败,请稍后再试。");
			// handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private static String space(int n){//构造n空格的string
		String space="";
		for(int i=0;i<n;i++)
			space+=" ";
		return space;
	}
}
