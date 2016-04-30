package driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Driver_PrintHelper {
	
	/* URL of database */
	private static final String URL = "jdbc:mysql://localhost:3306/save_automobile?autoResonnect=true&useSSL=false";
	
	/* Use default "root" as user */
	private static final String USER_NAME = "root";
	
	/* no password for "root" */
	private static final String PASSWORD = "Java@18641";

	
	/* Print the Automobile, OptionSet and AutoOption table in one run */
	public void printTable() {
		System.out.println("Print all tables");
		System.out.println();
		
		/* print Automobile table */
		System.out.println("Print Automobile");
		System.out.println("----------------------------------------------------------------------------------");
		printAutomobileTable();
		System.out.println("----------------------------------------------------------------------------------");
		
		/* Print OptionSet table */
		System.out.println("Print Option Set");
		System.out.println("----------------------------------------------------------------------------------");
		printOptionSetTable();
		System.out.println("----------------------------------------------------------------------------------");
		
		/* Print AutoOption table */
		System.out.println("Print Option");
		System.out.println("----------------------------------------------------------------------------------");
		printOptionTable();
		System.out.println("----------------------------------------------------------------------------------");
		System.out.println("Print All Tables End");
		
	}
	
	
	/* print the Automobile table */

	public void printAutomobileTable() {
		Connection connection = null;
		Statement statement = null;
		String command = null;
		ResultSet rs;
		try {
			
			// get connection
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			
			/* Get execution list */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/printdatabase.Properties");
				executelist.load(in);
				in.close();
			}  catch (IOException e){
				// Catch IOException
					System.out.println("Error .. " + e.toString());
			}
			
			/* Select all automobile */
			statement = connection.createStatement();
			command = executelist.getProperty("SelectAutomobile");
			rs = statement.executeQuery(command);
			
			/* print title */
			System.out.format("%-5s%20s%21s\n", "AutoId", "AutoName", "BasePrice");
			
			/* print each line */
			while (rs.next()) {
				System.out.format("%-18s%-20s%9s\n", rs.getString("AutoId"), rs.getString("AutoName"),
						rs.getString("BasePrice"));
			}
			
			statement.close();
			rs.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/* Print the OptionSet table*/


	public void printOptionSetTable() {
		Connection connection = null;
		Statement statement = null;
		String command = null;

		ResultSet rs;
		try {
			
			// get connection
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			statement = connection.createStatement();
			
			/* Get execution list */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/printdatabase.Properties");
				executelist.load(in);
				in.close();
			}  catch (IOException e){
				// catch IOException
					System.out.println("Error .. " + e.toString());
			}
			
			/* Select all automobile */
			statement = connection.createStatement();
			command = executelist.getProperty("SelectOptionSet");
			rs = statement.executeQuery(command);
			
			/* Print title */
			System.out.format("%10s%20s%16s\n", "OptionSetId", "OptionSetName", "AutoId");
			
			/* Print each line  */
			while (rs.next()) {
				System.out.format("%-18s%-20s%9s\n", rs.getString("OptionSetId"), rs.getString("OptionSetName"),
						rs.getString("AutoId"));

			}
			
			// close
			statement.close();
			rs.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} 

	}
	
	
	/* print the AutoOptionTable */

	public void printOptionTable() {
		Connection connection = null;
		Statement statement = null;
		String command = null;

		ResultSet rs;
		try {
			
			// get connection
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			statement = connection.createStatement();
			
			/* Get execution list */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/printdatabase.Properties");
				executelist.load(in);
				in.close();
			}  catch (IOException e){
				// catch IOException
					System.out.println("Error .. " + e.toString());
			}
			
			/* Select all automobile */
			statement = connection.createStatement();
			
			/* Select all options from autoOption */
			command = executelist.getProperty("SelectAutoOption");
			rs = statement.executeQuery(command);
			
			/* Print title */
			System.out.format("%-5s%20s%41s%15s%20s\n", "OptionId", "OptionName", "OptionPrice","OptionSetId","AutoID");
			
			/* Select each line */
			while (rs.next()) {
				System.out.format("%-18s%-40s%-15s%-15s%11s\n", rs.getString("OptionId"), rs.getString("OptionName"),
						rs.getString("OptionPrice"), rs.getString("OptionSetId"), rs.getString("AutoID"));
			}
			
			statement.close();
			rs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 

	}
}