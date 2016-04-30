package util;

import java.io.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Properties;
import model.Automobile;

public class DatabaseIO {
	private static final String URL = "jdbc:mysql://localhost:3306/save_automobile?autoResonnect=true&useSSL=false";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "Java@18641";

	/* Create a database */
	public void createDataBase() {
		Statement statement = null;
		BufferedReader br = null; // buffer reader
		try {

			/* Get connection */
			Connection connection = null;
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			if (connection != null) {
				System.out.println("Connected to Database successfully!");
			}
			statement = connection.createStatement();
			String command = null;

			/* Get the execute command list for execution */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/executedatabase.Properties");
				executelist.load(in);
				in.close();
			} catch (IOException e) {

				/* Catch IOException */
				System.out.println("Error .. " + e.toString());
			}

			/*
			 * Delete the database with a name "save_automobile" to avoid
			 * "DATABASE ALREADY EXISTS" exception
			 */

			try {
				command = executelist.getProperty("DeleteDataBase");
				statement.executeUpdate(command);
			} catch (Exception e) {

				/* If there is no such database, then do nothing and skip */
			}

			/* Insert auto name and base price in the Automobile table */
			br = new BufferedReader(new FileReader(new File("databaseinputfile/createdatabase.sql")));

			/* Read in the create database and tables command from file */
			while ((command = br.readLine()) != null) {
				statement.executeUpdate(command);
			}

			/* close the file stream */
			br.close();
			connection.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* Add an automobile to database */
	public int[] addToDatabase(Automobile auto, int autoID, int optSetIDStart, int optionIDStart) {

		/* autoID, optionSetID and optionID */
		int[] idrecorder = new int[2];
		int optionSetID = optSetIDStart;
		int optionID = optionIDStart;
		LinkedHashMap<String, Float> options = null;

		String[] optionSetName = { "Color", "Transmission", "ABS/Traction Control", "Side Impact Airbags",
				"Power Moonroof" };

		try {

			/* Get connection */
			Connection connection = null;
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			if (connection != null) {
				System.out.println("Connected to Database Successfully!");
			}

			/* Get the execute command list for execution */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/executedatabase.Properties");
				executelist.load(in);
				in.close();
			} catch (IOException e) {

				/* Catch IOException */
				System.out.println("Error .. " + e.toString());
			}

			/* Insert auto name and base price in Automobile table */
			PreparedStatement preparedStatement = connection.prepareStatement(executelist.getProperty("AddAutomobile"));
			preparedStatement.setInt(1, autoID);
			preparedStatement.setString(2, auto.getModelName());
			preparedStatement.setFloat(3, auto.getBasePrice());
			preparedStatement.executeUpdate();

			/* Insert option set name into OptionSet table */
			for (int i = 0; i < optionSetName.length; i++) {
				preparedStatement = connection.prepareStatement(executelist.getProperty("AddOptionSet"));
				preparedStatement.setInt(1, optionSetID);
				preparedStatement.setString(2, optionSetName[i]);
				preparedStatement.setInt(3, autoID);
				preparedStatement.executeUpdate();

				/* Insert options into AutoOption table */
				options = auto.getOptionSetMap(optionSetName[i]);
				for (String option : options.keySet()) {

					preparedStatement = connection.prepareStatement(executelist.getProperty("AddOption"));
					preparedStatement.setInt(1, optionID);
					preparedStatement.setString(2, option);
					preparedStatement.setFloat(3, options.get(option));
					preparedStatement.setInt(4, optionSetID);
					preparedStatement.setInt(5, autoID);
					preparedStatement.executeUpdate();

					optionID++;// Add option id
				}

				optionSetID++;// Add option set id
			}

			/* Set the new starter optionset id and option id */
			idrecorder[0] = optionSetID;
			idrecorder[1] = optionID;

			System.out.println("Added " + auto.getModelName() + " To Database Successfully!");

			// close connection
			preparedStatement.close();
			connection.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return idrecorder;
	}

	/* Delete an automobile from the database */
	public void deleteAutoInDatabase(String AutoName) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			if (connection != null) {
				System.out.println("Connected to Database Successfully!");
			}

			/* Get the execute command list for execution */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/executedatabase.Properties");
				executelist.load(in);
				in.close();
			} catch (IOException e) {

				/* Catch IOException */
				System.out.println("Error .. " + e.toString());
			}

			/* Get the AutoId for this AutoName */
			PreparedStatement preparedStatement = connection
					.prepareStatement(executelist.getProperty("SelectAutomobileByName"));
			preparedStatement.setString(1, AutoName);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			int AutoID = Integer.parseInt(rs.getString("AutoID"));

			/* Delete all options of this AutoId */
			preparedStatement = connection.prepareStatement(executelist.getProperty("DeleteAutoOption"));
			preparedStatement.setInt(1, AutoID);
			preparedStatement.executeUpdate();

			/* Delete all the option sets of this AutoId */
			preparedStatement = connection.prepareStatement(executelist.getProperty("DeleteOptionSet"));
			preparedStatement.setInt(1, AutoID);
			preparedStatement.executeUpdate();

			/* Delete the automobile of this AutoId */
			preparedStatement = connection.prepareStatement(executelist.getProperty("DeleteAutomobile"));
			preparedStatement.setInt(1, AutoID);
			preparedStatement.executeUpdate();

			System.out.println("Delete " + AutoName + " Success!");

			/* close the connection */
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/* Update automobile basePrice in the database */

	public void updateAutoBasePrice(String AutoName, float newBasePrice) {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			if (connection != null) {
				System.out.println("Connected to Database Successfully!");
			}

			/* Get the execute command list for execution */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/executedatabase.Properties");
				executelist.load(in);
				in.close();
			} catch (IOException e) {

				/* Catch IOException */
				System.out.println("Error .. " + e.toString());
			}

			/* Update the base price */
			PreparedStatement preparedStatement = connection
					.prepareStatement(executelist.getProperty("UpdateAutoBasePrice"));
			preparedStatement.setFloat(1, newBasePrice);
			preparedStatement.setString(2, AutoName);
			preparedStatement.executeUpdate();

			System.out.println("Update " + AutoName + " BasePrice Successfully");

			/* Close the connection */
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Automobile " + AutoName + " not in the database");
		}
	}

	/* Update an automobile option price in the database */

	public void updateAutoOptionPrice(String AutoName, String OptionSetName, String OptionName, float newOptionPrice) {

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
			if (connection != null) {
				System.out.println("Connected to Database Successfully!");
			}

			/* Get the execute command list for execution */
			Properties executelist = new Properties();
			try {
				FileInputStream in = new FileInputStream("databaseinputfile/executedatabase.Properties");
				executelist.load(in);
				in.close();
			} catch (IOException e) {

				/* Catch IOException */
				System.out.println("Error .. " + e.toString());
			}

			/* Get the AutoId for this AutoName */
			PreparedStatement preparedStatement = connection
					.prepareStatement(executelist.getProperty("SelectAutomobileByName"));
			preparedStatement.setString(1, AutoName);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			int AutoId = Integer.parseInt(rs.getString("AutoId"));

			/* Get the OptionId for this AutoName */
			preparedStatement = connection.prepareStatement(executelist.getProperty("GetOptionSetID"));
			preparedStatement.setString(1, OptionSetName);
			preparedStatement.setInt(2, AutoId);
			rs = preparedStatement.executeQuery();
			rs.next();
			int OptionSetId = Integer.parseInt(rs.getString("OptionSetId"));

			/*
			 * Change the option price with optionset id, auto id and option /*
			 * name
			 */
			preparedStatement = connection.prepareStatement(executelist.getProperty("UpdateOptionPrice"));
			preparedStatement.setFloat(1, newOptionPrice);
			preparedStatement.setInt(2, OptionSetId);
			preparedStatement.setInt(3, AutoId);
			preparedStatement.setString(4, OptionName);
			preparedStatement.executeUpdate();

			System.out.println("Update " + AutoName + " " + OptionSetName + " " + OptionName + " Successfully");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}