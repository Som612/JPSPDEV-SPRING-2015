package adapter;

public interface CreateAuto {
	/* When a text input file is given, this method is used to build an instance
	 * of Automobile. This method does not have to return the Auto instance*/

	public void buildAuto(String filename);
	
	/* This function searches and prints the properties of a given Automobile*/
	public void printAuto(String ModelName);
}