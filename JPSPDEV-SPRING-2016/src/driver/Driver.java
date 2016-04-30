package driver;

import adapter.BuildAuto;
import adapter.JDBCAuto;
import util.DatabaseIO;

public class Driver {
	public static void main(String[] args) {
		
		/* Create a database with name "save_automobile" 
		 * in the default superuser "root" it will drop the database with Name 
		 * "save_automobile" so we can run this Driver class multiple times */
		new DatabaseIO().createDataBase();
		
		/* Build the autos to execute JDBC relevant method */
		JDBCAuto autos = new BuildAuto();
		
		/* Add the autos to database and LinkedHashMap and see the result
		*from database */
		System.out.println("=============================================");
		System.out.println("Processing Adding Car To Database");
		System.out.println();
		autos.JDBC_buildAuto("databaseinputfile/Mustang.txt");
		autos.JDBC_buildAuto("databaseinputfile/Audi.txt");
		autos.JDBC_buildAuto("databaseinputfile/Focus.Properties");
		autos.JDBC_buildAuto("databaseinputfile/LFA.Properties");
		System.out.println("================================================");
		new Driver_PrintHelper().printTable();
		System.out.println();
		
		/* Delete the autos in database and LinkedHashMap and see the result
		 from database */
		System.out.println("==================================================");
		System.out.println("Processing Delete Car in Database");
		System.out.println();
		
		/* Try delete a car first */
		autos.JDBC_deleteAuto("Mustang");
		autos.JDBC_deleteAuto("Focus");
		autos.JDBC_deleteAuto("LFA");
		
		/* Try deleting the same car to see if cardoesnotexist exception 
		 *  "Error .. *********" */
		autos.JDBC_deleteAuto("Mustang");
		
		System.out.println("===============================================");
		new Driver_PrintHelper().printTable();
		System.out.println();
		
		/* Update the autos in database and LinkedHashMap and see the result
		* from database */
		System.out.println("===============================================");
		System.out.println("Processing Updating Car in Database");
		System.out.println();
		
		/* Update automobile's base price */
		autos.JDBC_updateAutoBasePrice("Audi", 18641);
		
		/* Update an option price */
		autos.JDBC_updateAutoOptionPrice("Audi", "Color", "Gold", 18641);
		
		/* Update an option price */
		autos.JDBC_updateAutoOptionPrice("Audi", "Power Moonroof", "Convertible",18641);
		
		/* Try and update a non existing base price, resulting in exception error */
		autos.JDBC_updateAutoBasePrice("Ford Mustang", 18641);
		
		/* Try and update a non existent Automobile option price, resulting in exception error*/
		autos.JDBC_updateAutoOptionPrice("Ford Mustang", "Power Moonroof", "Convertible",18641);
		
		System.out.println("===================================================");
		new Driver_PrintHelper().printTable();

		
	}
}