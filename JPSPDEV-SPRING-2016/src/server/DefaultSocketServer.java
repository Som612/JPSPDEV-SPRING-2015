package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class DefaultSocketServer extends Thread implements SocketServerInterface, SocketServerConstants {

	private int iPort;
	private Socket clientSocket = null;

	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream objectOutputStream = null;

	public DefaultSocketServer(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.iPort = PORT;
	}

	/* Run */
	public void run() {
		if (openConnection()) {
			handleSession();
			closeSession();
		}
	}

	public boolean openConnection() {
		try {
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		} catch (Exception e) {
			if (DEBUG)
				System.err.println("Unable to obtain stream to/from Echo Port" + iPort);
			return false;
		}
		return true;
	}

	public void handleSession() {
		String strInput;
		String strOutput;
		if (DEBUG)
			System.out.println("Handling Session in" + iPort);

		/* Successful connection */
		strOutput = "connect to " + iPort;
		try {
			objectOutputStream.writeObject(strOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BuildCarModelOptions buildCarModelOptions = new BuildCarModelOptions();

		while (true) { // Outer while loop
			try {

				/* Get option */
				strInput = (String) objectInputStream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				continue;
			}

			/* Quit if 0 */
			if (strInput.equals("0")) {
				break;
			} else if (strInput.equals("1") || strInput.equals("2") || strInput.equals("3") || strInput.equals("4")) {

			} else {
				System.out.println("Illegal Input");
				continue;
			}

			while (true) { // Inner while loop

				if (strInput.equals("1")) {

					/* Send get request reply */
					strOutput = "Get upload automobile request, Please input the .properties file number";
					try {
						objectOutputStream.writeObject(strOutput);
					} catch (IOException e) {
						e.printStackTrace();
					}

					/* Get file name and choose what to do */
					String fileName = null;
					try {
						fileName = (String) objectInputStream.readObject();

						/* Write file name to the stream */
						String output = "Get File Name: " + fileName;
						objectOutputStream.writeObject(output);

					} catch (ClassNotFoundException | IOException e) {
						fileName = "null";
					}

					/* Upload .Properties file */
					if (fileName.matches("[a-zA-Z0-9]+.Properties")) {

						/* Read .Properties file and build car */
						Properties prop = null;
						try {
							prop = (Properties) objectInputStream.readObject();
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}

						/* Add the car to the LinkedHashMap */
						buildCarModelOptions.buildAutoWithProperty(prop);

						/* Auto built successfully message */
						try {
							strOutput = "Build Auto successful";
							objectOutputStream.writeObject(strOutput);
						} catch (IOException e) {
							e.printStackTrace();
						}

						break;

					}

					/* If a text file is uploaded */

					else {
						try {
							File file = new File(fileName);
							FileOutputStream fileOutputStream = new FileOutputStream(file);
							BufferedInputStream bufferedInputStream = new BufferedInputStream(
									clientSocket.getInputStream());

							byte[] buf = new byte[10240];
							int len = 0;
							while ((len = bufferedInputStream.read(buf, 0, 10240)) > 0) {

								/*
								 * Read the file from client and write it to
								 * file
								 */
								fileOutputStream.write(buf, 0, len);
								fileOutputStream.flush();
								break;
							}
							fileOutputStream.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						/* Read the file back */
						buildCarModelOptions.buildAutoWithTxt(fileName);

						/* Delete the file at server end */
						File file = new File(fileName);
						file.delete();

						/* Auto built successfully message */
						strOutput = "Car built from text file successfuly";
						try {
							objectOutputStream.writeObject(strOutput);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}

				/* If its a configure request */
				else if (strInput.equals("2")) {

					/* Send the configure request */
					strOutput = "Get configure request";
					try {
						objectOutputStream.writeObject(strOutput);
					} catch (IOException e) {
						e.printStackTrace();
					}

					/* Get all auto name in the server and send it back */
					ArrayList<String> autoNameList = buildCarModelOptions.getAllModelList();
					try {
						objectOutputStream.writeObject(autoNameList);
					} catch (IOException e) {
						e.printStackTrace();
					}

					/* Send the saved auto list to client */
					strOutput = "transfer AutoList successfully";
					try {
						objectOutputStream.writeObject(strOutput);
					} catch (IOException e) {
						e.printStackTrace();
					}
					/* If there is no saved car break as a client */
					if (autoNameList.size() == 0) {
						break;
					}

					/* Get model name from server */
					String modelName = null;
					try {
						modelName = (String) objectInputStream.readObject();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}

					/* Send the selected auto mobile to client */
					try {
						buildCarModelOptions.sendSelectedModel(objectOutputStream, modelName);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					/* Transfer successful reply to client */

					strOutput = "Transfer automobile succesfully";
					try {
						objectOutputStream.writeObject(strOutput);
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				}
				/* Handle the servlet select car model request */
				else if (strInput.equals("3")) {

					/* Get all auto name in server and send back */
					ArrayList<String> autoNameList = buildCarModelOptions.getAllModelList();
					try {
						objectOutputStream.writeObject(autoNameList);
						objectOutputStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
					break;
				}
				/* Handle servlet get an automobile request */
				else if (strInput.equals("4")) {

					/* Get model name from server */
					String modelName = null;
					try {
						modelName = (String) objectInputStream.readObject();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}

					/* Send the selected auto mobile to client */
					try {
						buildCarModelOptions.sendSelectedModel(objectOutputStream, modelName);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					break;
				}
				break;
			} // inner while
		} // outer while
	}// handle session

	public void closeSession() {
		try {
			objectInputStream.close();
			objectInputStream.close();
			clientSocket.close();

		} catch (IOException e) {
			if (DEBUG)
				System.err.println("Error closing socket");
		}
	}
}