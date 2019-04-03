package dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

public class ConnectClass {
	String account;
	String password;
	int level;
	String URL = "jdbc:odbc:whdb";
	public ConnectClass(String account){
		this.account=account;
	}
	
	public ConnectClass(String account, String password,int level) {
		this.account = account;
		this.password = password;
		this.level=level;
	}

	public boolean initConnect() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn;
			String user="sa",pass="";
			if(level<1){
				user=account;pass=password;
				conn = DriverManager.getConnection(URL, user, pass);
			}else {
				String passwordHere="",sql;
				conn = DriverManager.getConnection(URL, user, pass);
				Statement stmt = conn.createStatement();
				if(level==1)
					sql="select ���� from teacher where ��ʦ�� = '"+account+"'";
				else
					sql="select ���� from student where ѧ�� = '"+account+"'";
				ResultSet rs =stmt.executeQuery(sql);
				rs.next();
			    passwordHere=rs.getString("����").trim();
				rs.close();
				stmt.close();
				return passwordHere.equals(password);
			}
			conn.close();
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		} catch (SQLException ex) {
			return false;
		}
	}

	public void connect() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			// ����
			Connection conn;
			conn = DriverManager.getConnection(URL );
			// ����statement
			Statement stmt = conn.createStatement();
			String sql = "select * from ���� where Ա���� = 'emp2008001' AND ���� = 'qkun'";
			// ִ��
			ResultSet rs = stmt.executeQuery(sql);
			//Ԫ����(meta-data)����
			ResultSetMetaData rsmd=rs.getMetaData();
			int n=rsmd.getColumnCount();
			String temp;
			
			for (int i = 1; i <= n; i++){
				temp=rsmd.getColumnLabel(i)+"("+rsmd.getColumnTypeName(i)+")";
				System.out.print(temp+space(20-temp.length()));
			}
			System.out.println();
			
			while (rs.next()) {
				// �����Ϣ
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
			JOptionPane.showMessageDialog(null, "�������ݿ�ʧ��,���Ժ����ԡ�");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "��������ʧ��,���Ժ����ԡ�");
		}
	}

	public String[][] querySQL(String sql){
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// ����
			Connection conn;
			conn = DriverManager.getConnection(URL);
			// ����statement
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd=rs.getMetaData();
			int col=rsmd.getColumnCount();
			Vector<String[]> v = new Vector<String[]>(1, 1);
			int num = 0;
			while (rs.next()) {
				String[] list = new String[col];
				for(int i=1;i<=col;i++)
					list[i-1]=rs.getString(i);
				v.add(list);
				num++;
			}
			if(num==0)
				return null;
			String[][] queryResult = new String[num][col];
			for (int i = 0; i < num; i++) {
				queryResult[i] = v.get(i);
			}
			rs.close();
			stmt.close();
			conn.close();
			return queryResult;
		} catch (ClassNotFoundException ex) {
			return null;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public boolean updateSQL(String sql){
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// ����
			Connection conn;
			conn = DriverManager.getConnection(URL);
			// ����statement
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.close();
			conn.close();
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		} catch (SQLException ex) {
			return false;
		}

	}
	
	private String space(int n){//����n�ո��string
		String space="";
		for(int i=0;i<n;i++)
			space+=" ";
		return space;
	}
	
	public static void main(String[] args) {
		ConnectClass conn = new ConnectClass("", "",0);
		conn.connect();
		String sql= "SELECT * FROM ְԱ 	    WHERE Ա���� = 'emp2008001'";
		String[][] data=conn.querySQL(sql);
		if(data!=null){
			for(int i=0;i<data.length;i++)
			{
				for(int j=0;j<data[0].length;j++)
					System.out.print(data[i][j]+"\t");
				System.out.println();
			}
					
		}
	}
}
