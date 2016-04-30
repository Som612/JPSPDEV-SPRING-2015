package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import exception.AutoException;
import exception.CustomExceptionEnum;

class OptionSet implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Option> options;
	private String name;
	private Option choiceOption;

	/* Constructor for option set */
	protected OptionSet(String name) {
		this.name = name;
		options = new ArrayList<Option>();
	}

	/* Get the name of the optionSet */
	protected String getOptionSetName() {
		return name;
	}

	/* Set option set name */
	protected void setOptionSetName(String name) {
		this.name = name;
	}

	/* [1] Get option by its size */
	protected int getOptionListSize() {
		return options.size();
	}
	
	/* [2] Get option by its name */
	protected Option getOption(String optionName) {
		for (Option op : options) {
			if (op.getOptionName().equals(optionName)) {
				return op;
			}
		}
		return null;
	}
	
	/* [3] Get option and option price in LinkedHashMap for servlet configuration */
	protected LinkedHashMap<String, Float> getAllOptionLHM() {
		LinkedHashMap<String, Float> optionsetmap = new LinkedHashMap<String,Float>();
		for(Option op : options){
			optionsetmap.put(op.getOptionName(), op.getPrice());
		}
		return optionsetmap;
	}
	
	/* Getting the option by its index (used for configuring car) */
	protected Option getOption(int index){
		return options.get(index);
	}

	/* Set option by option name */
	protected void setOption(String optionName, float price) {
		options.add(new Option(optionName, price));
	}

	/* Delete option by option name */
	protected void deleteOption(String optionName) {
		for (Option op : options) {
			if (op.getOptionName().equals(optionName)) {
				options.remove(op);
				return;
			}
		}
	}

	/* Update option price by option name */
	protected void updateOptionPrice(String optionName, float price)
			throws AutoException {
		if (getOption(optionName) == null) {
			throw new AutoException(CustomExceptionEnum.OptionNotFound);
		} else {
			getOption(optionName).setPrice(price);
		}
	}

	/* Print all options in order */
	protected void printAllOptions() {
		Option op = null;
		for (int i = 0; i < options.size(); i++) {
			op = options.get(i);
			System.out.println(i + ". " + op.getOptionName() + ":Price "
					+ String.format("%.2f", op.getPrice()));
		}
	}

	/* Set choice for selected option name */
	protected void setChoice(String optionName) {
		choiceOption = getOption(optionName);
	}
	
	/* Set choice using index (used for configuring car */
	protected void setChoice(int index) {
		choiceOption = getOption(index);
	}
	

	/* Get the option choice name for this option */
	protected String getChoiceName() {
		return choiceOption.getOptionName();
	}

	/* Set the option choice price */
	protected float getChoicePrice() {
		return choiceOption.getPrice();
	}

	/* ==== Inner class option starts here ==== */
	protected class Option implements Serializable {

		private static final long serialVersionUID = 1L;
		private String name;
		private float price;

		/* Constructor for option name and price */
		protected Option(String name, float price) {
			this.name = name;
			this.price = price;
		}

		/* Set the option name */
		protected void setOptionName(String option) {
			this.name = option;
		}

		/* Get option by its name */
		protected String getOptionName() {
			return name;
		}

		/* Set option price */
		protected void setPrice(float price) {
			this.price = price;
		}

		/* Get option price */
		protected float getPrice() {
			return price;
		}

	}

}