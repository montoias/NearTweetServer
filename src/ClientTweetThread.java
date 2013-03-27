import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dto.TweetDto;

public class ClientTweetThread extends Thread {
	String userId;
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public ClientTweetThread(String _userId) {
		userId = _userId;

		ClientInfo ci = ServerApplication.clients.get(userId);
		socket = ci.getSocket();
		ois = ci.getOis();
		oos = ci.getOos();
	}

	public void run() {

		TweetDto tweet;

		try {
			while (true) {
				tweet = (TweetDto) ois.readObject();
				tweet.setTweetId(ServerApplication.incrementCount());
				System.out.println(tweet.getTweet() + "" + tweet.getTweetId());

				// TODO Review
				for (ClientInfo ci : ServerApplication.clients.values()) {
					ci.getOos().writeObject(tweet);
					ci.getOos().flush();
				}

			}
		} catch (IOException e) {

			System.out.println("Fechei a socket");
			
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
