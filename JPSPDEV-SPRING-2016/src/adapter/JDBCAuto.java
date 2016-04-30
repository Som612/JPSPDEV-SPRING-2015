package adapter;

public interface JDBCAuto {
	
	/* Build an automobile in the database and LinkedHashMap*/
	public void JDBC_buildAuto(String fileName);
	
	/* Delete an automobile in the database and LinkedhashMap */
	public void JDBC_deleteAuto(String AutoName);
	
	/* Update base price of an automobile in the database & LinkedHashMap*/
	public void JDBC_updateAutoBasePrice(String AutoName, float newPrice);
	
	/* Update option price of an automobile in database and LinkedHashMap*/
	public void JDBC_updateAutoOptionPrice(String Autoname, String OptionSetName, String OptionName,
			float OptionPrice);
}
