package me.jacobturner.castfast;

import java.sql.*;
import java.util.ArrayList;

public class CastFastSQL {
	private Connection c;
	private Statement stmt;

	public CastFastSQL() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:shows.db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	public ArrayList<String> getNames() {
		ArrayList<String> showNames = new ArrayList<String>();
		try {
			stmt = c.createStatement();
			try {
				ResultSet rs = stmt.executeQuery( "SELECT * FROM SHOWS;" );
				while ( rs.next() ) {
					String name = rs.getString("name");
					showNames.add(name);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				String sql = "CREATE TABLE shows (" +
						"ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
						" name TEXT NOT NULL," +
						" dj TEXT NOT NULL," +
						" email TEXT NOT NULL," +
						" dateandtime TEXT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return showNames;
	}

	public ArrayList<String> getShow(String showName) {
		ArrayList<String> showData = new ArrayList<String>();
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM shows WHERE name IN ('" + showName + "');" );
			while ( rs.next() ) {
				showData.add(rs.getString("name"));
				showData.add(rs.getString("dj"));
				showData.add(rs.getString("email"));
				showData.add(rs.getString("dateandtime"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return showData;
	}
	
	public void addShow(String showName, String djs, String djEmail, String dateAndTime) {
		try {
			stmt = c.createStatement();
			int newID = 0;
			ResultSet rs = stmt.executeQuery( "SELECT * FROM shows;" );
			while ( rs.next() ) {
				newID++;
			}
			rs.close();
			stmt = c.createStatement();
			String sql = "INSERT INTO shows (ID,name,djs,email,dateandtime) " +
	                     "VALUES (" + newID + ", '" + showName + "', +" + djs + ", '" + djEmail + "', " + dateAndTime + " );";
			stmt.executeUpdate(sql);
			stmt.close();
		    c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateShow(String showName, String djs, String djEmail, String dateAndTime) {
		try {
			int showID = 0;
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM shows WHERE name IN ('" + showName + "');" );
			while ( rs.next() ) {
				showID = rs.getInt("ID");
			}
			rs.close();
			stmt = c.createStatement();
		    String sql = "UPDATE shows set name = " + showName + " where ID=" + Integer.toString(showID) + ";";
		    stmt.executeUpdate(sql);
		    sql = "UPDATE shows set dj = " + djs + " where ID=" + Integer.toString(showID) + ";";
		    stmt.executeUpdate(sql);
		    sql = "UPDATE shows set email = "+djEmail+" where ID="+Integer.toString(showID)+";";
		    stmt.executeUpdate(sql);
		    sql = "UPDATE shows set dateandtime = " + dateAndTime + " where ID=" + Integer.toString(showID) + ";";
		    stmt.executeUpdate(sql);
			stmt.close();
		    c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
	    try {
			stmt.close();
		    c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
