package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements SocketServerConstants{
	private ServerSocket serverSocket = null;
	
	public Server() {
		try {
			/* Open this server */
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Listening on port " + PORT);
	}
	
	public void runServer() {
		DefaultSocketServer defaultClientSocket = null;
		try {
			while(true) {
				/* Accept the client */
				Socket clientSocket = serverSocket.accept();
				
				/* Use a defaultClientSocket to handle the session */
				defaultClientSocket = new DefaultSocketServer(clientSocket);
	            defaultClientSocket.start();
			}
        } catch (IOException e) {
        		e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		Server server = new Server();
		/* Start server */
		server.runServer();
	}
}