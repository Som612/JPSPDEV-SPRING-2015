package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

public interface AutoServer {

	/* Build automobile from .Properties file*/
	public void buildAutoWithProperty(Properties carProperties);
	
	/* Build automobile from .txt file*/
	public void buildAutoWithTxt(String fileName);
	
	/* Return all available automobile in LinkedHashMap*/
	public ArrayList<String> getAllModelList();
	
	/* Return selected model to client */
	public void sendSelectedModel(ObjectOutputStream objectOutputStream, String modelName) throws IOException;
	
}
