package server;

public interface SocketServerInterface {
	
	/* Open connection */
	boolean openConnection();
	
	/* Handle session */
	void handleSession();
	
	/* Close Session*/
	void closeSession();

}
