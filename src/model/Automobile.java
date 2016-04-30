package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import exception.AutoException;
import exception.CustomExceptionEnum;
import model.OptionSet.Option;

public class Automobile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String modelName;
	private float basePrice;
	private ArrayList<OptionSet> optionSets;
	private ArrayList<Option> choice;

	/* Base information start here */

	/* Constructor for Automobile */
	public Automobile(String modelName, float basePrice) {

		this.modelName = modelName;
		this.basePrice = basePrice;
		this.optionSets = new ArrayList<OptionSet>();
		this.choice = new ArrayList<Option>();
	}

	/* Getter for model name */
	public String getModelName() {
		return modelName;
	}

	/* Set the model name */
	public void setModel(String modelName) {
		this.modelName = modelName;
	}

	/* Get the base price */
	public float getBasePrice() {
		return basePrice;
	}

	/* Set the base price */
	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	/* printBaseInfo() */
	public void printBaseInfo() {
		System.out.println(this.getModelName());
		System.out.println("Base Price :" + String.format("%.2f", this.getBasePrice()));
		System.out.println();
	}

	/* ==== Option Set Start Here ====== */

	/*
	 * [1] get option set size
	 * 
	 * [2] get option set by name
	 * 
	 * [3] get the option set all options and corresponding price in
	 * linkedhashmap, used for servlet
	 */
	public int getOptionSetListSize() {
		return optionSets.size();
	}

	protected OptionSet getOptionSet(String setName) {
		for (OptionSet opset : optionSets) {
			if (opset.getOptionSetName().equals(setName)) {
				return opset;
			}
		}
		return null;
	}

	public LinkedHashMap<String, Float> getOptionSetMap(String optionSetName) {
		LinkedHashMap<String, Float> optionMap = getOptionSet(optionSetName).getAllOptionLHM();
		return optionMap;
	}

	/* Get option set name - testing thread method */
	public String getOptionSetName(String optionSetName) throws AutoException {
		if (getOptionSet(optionSetName) == null) {
			throw new AutoException(CustomExceptionEnum.OptionSetNotFound);
		} else {
			return getOptionSet(optionSetName).getOptionSetName();
		}

	}

	/* Set an option set by its name */
	public void setOptionSet(String setName) {
		optionSets.add(new OptionSet(setName));
	}

	/* Delete an option set by its name */
	public void deleteOptionSet(String setName) {

		for (OptionSet opset : optionSets) {
			if (opset.getOptionSetName().equals(setName)) {
				optionSets.remove(opset);
				return;
			}
		}
	}

	/* Update an option set by its name */
	public void updateOptionSetName(String setName, String newSetName) throws AutoException {
		boolean updateFlag = false;
		for (OptionSet opset : optionSets) {
			if (opset.getOptionSetName().equals(setName)) {
				opset.setOptionSetName(newSetName);
				return;

			}
		}
		if (!updateFlag) {
			throw new AutoException(CustomExceptionEnum.OptionSetNotFound);
		}

	}

	/* Print all option set */
	public void printAllOptionSet() {
		for (OptionSet opset : optionSets) {
			System.out.println(opset.getOptionSetName() + ":");
			opset.printAllOptions();
			System.out.println();
		}
	}

	// used for client configurate car
	public String printOptionSetName(int i) {
		return optionSets.get(i).getOptionSetName();
	}

	/* ========== Access to Option Start Here =========== */

	/* Get option by option set name and option name */
	protected Option getOption(String setName, String optionName) {
		if (getOptionSet(setName) != null) {
			return getOptionSet(setName).getOption(optionName);
		}
		return null;
	}

	/* Get option price by option setName and option name */
	public float getOptionPrice(String setName, String optionName) {
		if (getOptionSet(setName) != null) {
			return getOption(setName, optionName).getPrice();
		}
		return 0;
	}

	/* Set option by option set name */
	public void setOption(String setName, String optionName, float price) {
		if (getOptionSet(setName) != null) {
			getOptionSet(setName).setOption(optionName, price);
		}

	}

	/* Delete option by option set name and option name */
	public void deleteOption(String setName, String optionName) {
		if (getOptionSet(setName) != null) {
			getOptionSet(setName).deleteOption(optionName);
		}
	}

	/* Update option price by option set name and option name */
	public void updateOptionPrice(String setName, String optionName, float price) throws AutoException {
		if (getOptionSet(setName) != null) {
			getOptionSet(setName).updateOptionPrice(optionName, price);
		} else {
			throw new AutoException(CustomExceptionEnum.OptionSetNotFound);
		}
	}

	public void printOption(String setName) {
		getOptionSet(setName).printAllOptions();
	}

	/* Used for configuration */
	public int getOptionListSize(String optionSetName) {
		int length = getOptionSet(optionSetName).getOptionListSize();
		return length;
	}

	/* Print all option set and option */
	public void print() {
		this.printBaseInfo();
		this.printAllOptionSet();
	}

	/* ==== Access to Choice Starts Here ==== */

	/* Set option choice and add it to the list */
	public void setOptionChoice(String setName, String optionName) {
		OptionSet opset = getOptionSet(setName);
		choice.add(opset.getOption(optionName));
		opset.setChoice(optionName);
	}

	/* Method for car configuration */
	public void setOptionChoice(String setName, int index) {
		OptionSet opset = getOptionSet(setName);
		choice.add(opset.getOption(index));
		opset.setChoice(index);
	}

	/* Getter for Option Choice */
	public String getOptionChoice(String setName) {
		return getOptionSet(setName).getChoiceName();
	}

	/* Get option choice price */
	public float getOptionChoicePrice(String setName) {
		return getOptionSet(setName).getChoicePrice();
	}

	/* Get total price of all the choices selected */
	public float getTotalPrice() {
		float sum = basePrice;
		for (Option op : choice) {
			sum += op.getPrice();
		}
		return sum;
	}

	/* Print all the choice selected */
	public void printChoice() {

		for (OptionSet opSet : optionSets) {
			System.out.println(opSet.getOptionSetName() + ":" + opSet.getChoiceName());
		}

		printTotalPrice();
	}

	/* Print price for all selected choices */
	public void printTotalPrice() {
		System.out.println("Total price : " + getTotalPrice());
	}

}