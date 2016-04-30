package adapter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import scale.*;
import util.DatabaseIO;
import util.FileIO;
import exception.*;
import model.*;

public abstract class ProxyAutomobile {
	/*
	 * This class will contain all the implementation of any method declared in
	 * the interface
	 */
	private static int threadID = 0; // only for the use of thread method
	private static LinkedHashMap<String, Automobile> autoList = new LinkedHashMap<String, Automobile>();

	/* Three distinct IDs for the database table */

	/* Current ID of car */
	private static int AutoID = 0;

	/* Current option set ID */
	private static int OptionSetID = 0;

	/* Current option ID */
	private static int OptionID = 0;

	/*
	 * Build an automobile and set it in Fleet exception : FilenotFound
	 */
	public void buildAuto(String fileName) {
		try {
			Automobile auto = new FileIO().readInAutomobile(fileName);
			autoList.put(auto.getModelName(), auto);

		} catch (AutoException ae) {
			fix(ae.getErrorNumber());
		}

	}

	/* Build an automobile and set it into the LinkedHashMap and database */
	public void JDBC_buildAuto(String filename) {

		Automobile auto = null;

		/* Read an automobile from file */
		try {
			if (filename.matches("[a-zA-Z0-9/]+.Properties")) {
				auto = new FileIO().readInAutoPropertyFile(filename);
			} else {
				auto = new FileIO().readInAutomobile(filename);

			}

		} catch (AutoException ae) {
			fix(ae.getErrorNumber());
		}

		/* If the automobile is already in the LHM, then return */
		if (autoList.get(auto.getModelName()) != null) {
			return;
		} else {
			autoList.put(auto.getModelName(), auto);
			// boolean check = autoList.containsValue("Mustang.txt");
			// System.out.println("what is in LHM " + check);
			/*
			 * Refresh the current ID for new input to thus ensuring all the IDs
			 * in the table are distinct
			 */
			int[] idrecorder = new DatabaseIO().addToDatabase(auto, AutoID, OptionSetID, OptionID);
			AutoID++;
			OptionSetID = idrecorder[0];
			OptionID = idrecorder[1];
		}

	}

	/*
	 * Print all the automobile info Exception : CarModelNotFound
	 */
	public void printAuto(String modelName) {
		Automobile auto = autoList.get(modelName);
		try {
			if (auto == null) {
				throw new AutoException(CustomExceptionEnum.CarModelNotFound);
			} else {
				auto.print();
			}
		} catch (AutoException ae) {
			System.out.println("Error .. " + ae.toString());
		}

	}

	/*
	 * Delete the model Exception : CarModelNotFound
	 */
	public void deleteAuto(String modelName) {
		Automobile auto = autoList.get(modelName);
		try {
			if (auto == null) {
				throw new AutoException(CustomExceptionEnum.CarModelNotFound);
			} else {
				autoList.remove(modelName);
			}
		} catch (AutoException ae) {
			System.out.println("Error .. " + ae.toString());
		}
	}

	/* JDBC_delete : Delete the model in the LHM and database */
	public void JDBC_deleteAuto(String AutoName) {
		if (autoList.get(AutoName) != null) {
			autoList.remove(AutoName);
			new DatabaseIO().deleteAutoInDatabase(AutoName);

			/* Delete it from the LHM too */
		} else {
			System.out.println("Error .. " + AutoName + " Model Not Found in database!");
		}
	}

	/*
	 * Update optionSetName Exception : CarModelNotFound
	 */

	public void updateOptionSetName(String ModelName, String OptionSetName, String newName) {
		Automobile auto = autoList.get(ModelName);
		try {

			if (auto != null) {
				auto.updateOptionSetName(OptionSetName, newName);
				autoList.put(ModelName, auto);
			} else {
				throw new AutoException(CustomExceptionEnum.CarModelNotFound);
			}

		} catch (AutoException ae) {
			System.out.println("Error .. " + ae.toString());
		}

	}

	/*
	 * Edit option depending upon the argument Exception : CarModelNotFound
	 */
	public void edit(EditOptionEnum editOptionEnum, String[] args) {
		Automobile auto = autoList.get(args[0]); /* args[0] is model name */
		try {
			if (auto == null) {
				throw new AutoException(CustomExceptionEnum.CarModelNotFound);
			} else {
				EditOption edit = new EditOption(++threadID, auto, editOptionEnum, args);
				edit.start();
			}
		} catch (AutoException ae) {
			System.out.println("Error .. " + ae.toString());
		}

	}

	/*
	 * Update optionPrice Exception : CarModelNotFound
	 */
	public void updateOptionPrice(String ModelName, String OptionName, String Option, float newprice) {
		Automobile auto = autoList.get(ModelName);
		try {
			if (auto != null) {
				auto.updateOptionPrice(OptionName, Option, newprice);
				autoList.put(ModelName, auto);
			} else {
				throw new AutoException(CustomExceptionEnum.CarModelNotFound);
			}
		} catch (AutoException ae) {
			System.out.println("Error .. " + ae.toString());
		}
	}

	/* JDBC update autobaseprice, update the base price in the database */
	public void JDBC_updateAutoBasePrice(String autoName, float newprice) {
		Automobile auto = autoList.get(autoName);
		if (auto != null) {
			auto.setBasePrice(newprice);
			new DatabaseIO().updateAutoBasePrice(autoName, newprice);
		} else {
			System.out.println("Error .. " + autoName + " not exist!");
		}
	}

	/* Update one of the options price in the database */
	public void JDBC_updateAutoOptionPrice(String AutoName, String OptionSetName, String OptionName,
			float newOptionPrice) {
		Automobile auto = autoList.get(AutoName);
		if (auto != null) {
			new DatabaseIO().updateAutoOptionPrice(AutoName, OptionSetName, OptionName, newOptionPrice);
		} else {
			System.out.println("Error .. " + AutoName + " not exist!");
		}
	}

	/*
	 * Save a .ser car model Exception : CarModelNotFound
	 */
	public void saveCarModel(String modelName) {
		Automobile auto = autoList.get(modelName);
		try {
			if (auto == null) {
				throw new AutoException(CustomExceptionEnum.CarModelNotFound);
			}
			new FileIO().serializeOutput(auto);
		} catch (AutoException ae) {
			System.out.println("Error .. " + ae.toString());
		}
	}

	/*
	 * Load a car model in .ser format. if not found, upload the
	 * Default_Lexus_LFA.ser
	 */
	public void loadCarModel(String modelName) {
		StringBuffer sb = new StringBuffer();
		sb.append(modelName);
		sb.append(".ser");

		try {
			Automobile auto = new FileIO().serializeInput(sb.toString());
			autoList.put(auto.getModelName(), auto);
		} catch (AutoException ae) {
			fix(ae.getErrorNumber());
		}
	}

	/* Build a car with input .Properties file */
	public void buildAutoWithProperty(Properties carProperties) {
		Automobile auto = new FileIO().readInAutomobileProperty(carProperties);
		autoList.put(auto.getModelName(), auto);
	}

	/* Build a car with a .txt file */
	public void buildAutoWithTxt(String fileName) {
		try {
			Automobile auto = new FileIO().readInAutomobile(fileName);
			autoList.put(auto.getModelName(), auto);
		} catch (AutoException e) {
			e.printStackTrace();
		}
	}

	/* Return all the available models in the LinkedHashMap */
	public ArrayList<String> getAllModelList() {
		ArrayList<String> autoNameList = new ArrayList<String>();
		for (String key : autoList.keySet()) {
			autoNameList.add(key);
		}
		return autoNameList;
	}

	/* Send the selected car from the server to the client */
	public void sendSelectedModel(ObjectOutputStream objectOutputStream, String modelName) throws IOException {
		Automobile selectedAuto = autoList.get(modelName);
		objectOutputStream.writeObject(selectedAuto);
	}

	/* Fix exception depending on the error number */
	public void fix(int errno) {

		FixHelper fixHelper = new FixHelper();

		switch (errno) {
		case 1:
			fixHelper.FixFileNotFound(autoList);
			break;
		case 5:
			fixHelper.SavedCarFileNotFound(autoList);
			break;
		default:
			break;
		}
	}

}