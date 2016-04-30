package exception;

import java.util.LinkedHashMap;

import model.Automobile;
import util.FileIO;

public class FixHelper {
	
	/* Default input car model - Focus*/

	private static final String DEFAULT_CAR_MODEL_FILE = "Default_Car_Model.Properties";
	
	/* Default load car model - Nissan GTR*/
	
	private static final String DEFAULT_SAVED_CAR_MODEL = "Default_NISSAN_GTR.ser";
	
	/* Fix file not found exception by loading default car model and set it into input fleet*/
	
	public void FixFileNotFound(LinkedHashMap<String, Automobile> autoList){
		Automobile auto = null;
		
		try{
			auto = new FileIO().readInAutomobile(DEFAULT_CAR_MODEL_FILE);
		} catch (AutoException e) {
			System.out.println("Default Model File not found excpetion: " + 
		e.toString());
		}
		autoList.put(auto.getModelName(), auto);
	}
	
	/* Fix missing base price by setting it to 0*/
	public String FixFileMissingBasePrice(){
		return "0";
	}
	
	/* Fix missing option price problem by setting it to 0 */
	public String[] FixFileMissingOptionPrice(String [] input){
		String[] output = new String[2];
		output[0] = input[0];
		output[1] = "0";
		return output;
	}
	
	/* Fix File not found exception by loading default car model and set it into input fleet */
	public void SavedCarFileNotFound(LinkedHashMap<String, Automobile> autoList){
		Automobile auto = null;
		try {
			auto = new FileIO().serializeInput(DEFAULT_SAVED_CAR_MODEL);
		} catch (AutoException e){
			System.out.println("Default Model File Not Found Exception: " +
		e.toString());
		}
		autoList.put(auto.getModelName(),auto);
	}
	
}
