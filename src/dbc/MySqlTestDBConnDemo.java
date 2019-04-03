package dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class MySqlTestDBConnDemo {

	public void connect() {
		try {
			String user = "scott";
			String password = "tiger";
			String databaseName = "test";
			String table = "test";
			
			Class.forName("com.mysql.jdbc.Driver");
			String URL = "jdbc:mysql://localhost:3306/"+databaseName+"?"
					+ "user="+user+"&password="+password;
			// ����
			Connection conn;
			conn = DriverManager.getConnection(URL);
			// ����statement
			Statement stmt = conn.createStatement();
			String sql = "select * from "+table;
			// ִ��
			ResultSet rs = stmt.executeQuery(sql);
			// Ԫ����(meta-data)����
			ResultSetMetaData rsmd = rs.getMetaData();
			int n = rsmd.getColumnCount();
			String temp;

			for (int i = 1; i <= n; i++) {
				temp = rsmd.getColumnLabel(i) + "(" + rsmd.getColumnTypeName(i)
						+ ")";
				System.out.print(temp + space(20 - temp.length()));
			}
			System.out.println();

			int rowCount=0;
			while (rs.next()) {
				// �����Ϣ
				for (int i = 1; i <= n; i++) {
					temp = rs.getString(i);
					if (temp == null) {
						temp = "null";
					}
					System.out.print(temp + space(20 - temp.length()));
				}
				System.out.println();
				rowCount++;
			}
			System.out.println("һ��"+rowCount+"��.");

			rs.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "�������ݿ�ʧ��,���Ժ����ԡ�");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "��������ʧ��,���Ժ����ԡ�");
			// handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private String space(int n) {// ����n�ո��string
		String space = "";
		for (int i = 0; i < n; i++)
			space += " ";
		return space;
	}

	public static void main(String[] args) {
		MySqlTestDBConnDemo conn = new MySqlTestDBConnDemo();
		conn.connect();
	}
}
