package pt.ist.meic.cmov.neartweet.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class ServerApplication {
	public static HashMap<String, ClientInfo> clients = new HashMap<String, ClientInfo>();
	public static ServerSocket serverSocket;
	public static int tweetId = 0;

	public static synchronized int incrementCount() {
		return tweetId++;
	}

	public static void main(String[] args) {

		try {
			ServerApplication.serverSocket = new ServerSocket(8081); // Server
																		// socket
			System.out.println("Server started. Listening to the port 8081");

			new RegisterThread().start();

		} catch (IOException e) {
			System.out.println("Could not listen on port: 8080");
		}

	}
}