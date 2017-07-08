package com.dbc.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!
public class LoadDriver {
	public static void main(String[] args) {
		Connection conn = null;
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=014789");
			// Do something with the Connection

			testSelect(conn);

			testProcedure(conn);

		} catch (SQLException ex) {
			ex.printStackTrace();
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqlEx) {
				} // ignore
				conn = null;
			}
		}
	}

	public static void testSelect(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT foo FROM bar");
			// or alternatively, if you don't know ahead of time that
			// the query will be a SELECT...
			if (stmt.execute("SELECT foo FROM bar")) {
				rs = stmt.getResultSet();
			}
			// Now do something with the ResultSet ....
			while (rs.next()) {
				System.out.println(rs.getString("foo"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Connector/J: Calling Stored Procedures
	 * 
	 * <pre>
	 * CREATE PROCEDURE demoSp(IN inputParam VARCHAR(255), \
	 *                         INOUT inOutParam INT)
	 * BEGIN
	 *     DECLARE z INT;
	 *     SET z = inOutParam + 1;
	 *     SET inOutParam = z;
	 *     SELECT inputParam;
	 *     SELECT CONCAT('zyxw', inputParam);
	 * END
	 * </pre>
	 */
	public static void testProcedure(Connection conn) {
		CallableStatement cStmt = null;
		ResultSet rs = null;
		try {
			//
			// Prepare a call to the stored procedure 'demoSp'
			// with two parameters
			//
			// Notice the use of JDBC-escape syntax ({call ...})
			//
			cStmt = conn.prepareCall("{call demoSp(?, ?)}");
			cStmt.setString(1, "abcdefg");
			//
			// Connector/J supports both named and indexed
			// output parameters. You can register output
			// parameters using either method, as well
			// as retrieve output parameters using either
			// method, regardless of what method was
			// used to register them.
			//
			// The following examples show how to use
			// the various methods of registering
			// output parameters (you should of course
			// use only one registration per parameter).
			//
			//
			// Registers the second parameter as output, and
			// uses the type 'INTEGER' for values returned from
			// getObject()
			//
			// cStmt.registerOutParameter(2, Types.INTEGER);
			//
			// Registers the named parameter 'inOutParam', and
			// uses the type 'INTEGER' for values returned from
			// getObject()
			//
			cStmt.registerOutParameter("inOutParam", Types.INTEGER);
			//
			// Set a parameter by index
			//
			// cStmt.setString(1, "abcdefg");
			//
			// Alternatively, set a parameter using
			// the parameter name
			//
			cStmt.setString("inputParam", "abcdefg");
			//
			// Set the 'in/out' parameter using an index
			//
			cStmt.setInt(2, 1);
			//
			// Alternatively, set the 'in/out' parameter
			// by name
			//
			cStmt.setInt("inOutParam", 1);
			boolean hadResults = cStmt.execute();
			//
			// Process all returned result sets
			//
			while (hadResults) {
				rs = cStmt.getResultSet();
				// process result set
				while (rs.next()) {
					System.out.println(rs.getString(1));
				}
				hadResults = cStmt.getMoreResults();
			}
			//
			// Retrieve output parameters
			//
			// Connector/J supports both index-based and
			// name-based retrieval
			//
			int outputValue = cStmt.getInt(2); // index-based
			// outputValue = cStmt.getInt("inOutParam"); // name-based
			System.out.println(outputValue);

		} catch (SQLException ex) {
			ex.printStackTrace();
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (cStmt != null) {
				try {
					cStmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				cStmt = null;
			}
		}
	}
}
