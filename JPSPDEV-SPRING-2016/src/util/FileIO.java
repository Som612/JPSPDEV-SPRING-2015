package util;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import exception.*;
import model.Automobile;

public class FileIO {

	/* Read in a automobile from .Properties file */
	public Automobile readInAutoPropertyFile(String filename) throws AutoException {
		Properties props = new Properties();
		Automobile auto = null;
		try {
			FileInputStream in = new FileInputStream(filename);
			props.load(in);
		} catch (FileNotFoundException e) {
			/* if file not found, fix it by FixHelper */
			throw new AutoException(CustomExceptionEnum.FileNotFound);
		} catch (IOException e) {
			/* catch IOException */
			System.out.println("Error -- " + e.toString());
		}

		auto = readInAutomobileProperty(props);
		return auto;

	}

	public Automobile readInAutomobileProperty(Properties props) {
		Automobile auto = null;
		auto = new Automobile(props.getProperty("CarModel"), Float.parseFloat(props.getProperty("BasePrice")));

		String option = "Option";
		String optionValue = "OptionValue";
		String optionPrice = "OptionPrice";

		for (char optionNum = '1'; props.getProperty(option + optionNum) != null; optionNum++) {
			auto.setOptionSet(props.getProperty(option + optionNum));

			for (char optionValueNum = 'a'; props
					.getProperty(optionValue + optionNum + optionValueNum) != null; optionValueNum++) {
				auto.setOption(props.getProperty(option + optionNum),
						props.getProperty(optionValue + optionNum + optionValueNum),
						Float.parseFloat(props.getProperty(optionPrice + optionNum + optionValueNum)));
			}

		}

		return auto;

	}

	public Automobile readInAutomobile(String filename) throws AutoException {

		Automobile auto = null;
		BufferedReader br = null;
		String line;

		/* Store model and basePrice */
		String[] baseInfo = new String[2];

		String opsetName = null;

		/* Store string without splitting */
		String[] storeOptionString;

		/* Store option detail */
		String[] storeOptionDetail;

		try {

			br = new BufferedReader(new FileReader(new File(filename)));

			/* Read base information */

			for (int i = 0; i < baseInfo.length; i++) {
				baseInfo[i] = br.readLine();
			}

			try {

				/* If basePrice is missing, fix it internally FixHelper ? */
				if (baseInfo[1].length() == 0) {
					throw new CustomIOException(CustomExceptionEnum.FileMissingBasePrice);
				}
			} catch (CustomIOException e) {
				FixHelper fixHelper = new FixHelper();
				baseInfo[1] = fixHelper.FixFileMissingBasePrice();
			}

			auto = new Automobile(baseInfo[0], Float.parseFloat(baseInfo[1]));

			/* Get option set size */
			while ((opsetName = br.readLine()) != null) {
				auto.setOptionSet(opsetName);

				/* get options and their prices */
				line = br.readLine();
				storeOptionString = line.split(";");
				for (int i = 0; i < storeOptionString.length; i++) {
					storeOptionDetail = storeOptionString[i].split(",");
					try {

						/*
						 * If the length of storeOptionDetail is not 1, option
						 * price is missing handle it by setting the price to 0
						 */
						if (storeOptionDetail.length != 1) {
							throw new CustomIOException(CustomExceptionEnum.FileOptionPriceNotFound);
						}
					} catch (CustomIOException e) {
						FixHelper fixHelper = new FixHelper();
						storeOptionDetail = fixHelper.FixFileMissingOptionPrice(storeOptionDetail);
					}

					auto.setOption(opsetName, storeOptionDetail[0], Float.parseFloat(storeOptionDetail[1]));
				}
			}

		} catch (FileNotFoundException fe) {

			/* If FileNotFound, fix it externally using FixHelper */
			throw new AutoException(CustomExceptionEnum.FileNotFound);
		} catch (IOException e) {

			// Catch IOException
			System.out.println("Error .. " + e.toString());
		} finally {
			try {
				/* If file is not found, it will be null */
				if (br != null) {
					br.close();
				}
			} catch (IOException brCloseException) {
				/* Catch IOException for br.close() */
				System.out.println("Error .. " + brCloseException.toString());
			}
		}
		/* return the value */

		return auto;
	}

	public void serializeOutput(Automobile auto) {
		ObjectOutputStream os = null;
		try {

			/* Write object to auto.ser file */

			/* StringBuffer to build file name for output */
			StringBuffer sb = new StringBuffer();
			sb.append(auto.getModelName());
			sb.append(".ser");
			os = new ObjectOutputStream(new FileOutputStream(sb.toString()));
			os.writeObject(auto);
			os.close();

		} catch (IOException e) {

			/* Catch stream close exception */
			System.out.println("Error .. " + e.toString());
			System.exit(1);
		} finally {
			try {
				/* Close the stream anyway */
				os.close();
			} catch (IOException streamCloseException) {
				/* Catch stream close exception */
				System.out.println("Error .. " + streamCloseException.toString());
			}
		}
	}

	public Automobile serializeInput(String filename) throws AutoException {
		ObjectInputStream is = null;
		Automobile auto = null;
		try {

			/* get object from "filename" */
			is = new ObjectInputStream(new FileInputStream(filename));
			auto = (Automobile) is.readObject();

		} catch (FileNotFoundException fe) {
			/* If file not found, fix it outside by FixHelper */
			throw new AutoException(CustomExceptionEnum.SavedCarFileNotFound);
		} catch (IOException e) {

			/* catch IOException */
			System.out.println("Error .. " + e.toString());
			System.exit(1);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				/* Close input stream anyhow */
				if (is != null) {
					is.close();
				}

			} catch (IOException streamCloseException) {
				/* Catch stream close exception */
				System.out.println("Error .. " + streamCloseException.toString());
			}
		}
		return auto;
	}

	public String[] getAutoFileList(String fileName) {
		BufferedReader br = null; // buffer reader
		ArrayList<String> autoArrayList = new ArrayList<String>();
		String file = null;
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			while ((file = br.readLine()) != null) {
				autoArrayList.add(file);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] autoNameList = new String[autoArrayList.size()];
		for (int i = 0; i < autoNameList.length; i++) {
			autoNameList[i] = autoArrayList.get(i);
		}
		return autoNameList;
	}

}