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
			ResultSet rs = stmt.executeQuery( "SELECT * FROM SHOWS;" );
			while ( rs.next() ) {
				String name = rs.getString("name");
				showNames.add(name);
			}
			rs.close();
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
	
	public void close() {
	    try {
			stmt.close();
		    c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
