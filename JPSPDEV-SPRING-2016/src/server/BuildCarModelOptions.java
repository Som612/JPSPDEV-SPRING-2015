package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import adapter.BuildAuto;

public class BuildCarModelOptions implements AutoServer {

	/*
	 * [1] Accept properties object from the client socket over an object stream
	 * and create an automobile [2] Add that created Automobile to the
	 * LinkedHashMap [3] AutoServer interface should be implemented in BuildAuto
	 * and BuildCarModelOptions
	 */

	/*
	 * Static buildAuto object for saving the car model and operate the
	 * linkedHashMap of automobile
	 */
	public static BuildAuto buildAuto;

	/* Constructor */
	public BuildCarModelOptions() {
		buildAuto = new BuildAuto();
	}

	/* Build auto with a .properties file */
	public void buildAutoWithProperty(Properties carProperties) {
		buildAuto.buildAutoWithProperty(carProperties);
	}

	/* Build auto with .txt file */
	public void buildAutoWithTxt(String fileName) {
		buildAuto.buildAutoWithTxt(fileName);
	}

	/* Return all available car models in the server */
	public ArrayList<String> getAllModelList() {
		ArrayList<String> autoNameList = buildAuto.getAllModelList();
		return autoNameList;
	}

	/* Send the selected model to the client */
	public void sendSelectedModel(ObjectOutputStream objectOutputStream, String modelName) throws IOException {
		buildAuto.sendSelectedModel(objectOutputStream, modelName);

	}

}
