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
				int id = ServerApplication.incrementCount();
				tweet.setTweetId(id);
				
				if (tweet.getConversationID() == -1)
					tweet.setConversationID(id);

				System.out.println("@" + tweet.getSender() + " "
						+ tweet.getTweet() + " id:" + tweet.getTweetId()
						+ "conversation id:" + tweet.getConversationID());

				for (String receiver : ServerApplication.clients.keySet()) {
					if (tweet.getTweet().contains("@" + receiver)) {
						ClientInfo ci = ServerApplication.clients.get(receiver);
						ci.getOos().writeObject(tweet);
						ci.getOos().flush();
						tweet.setReceiver(receiver);
						System.out.println("To: " + receiver);
					}
				}

				if (tweet.getReceiver() == null) {
					System.out.println("BroadCast");
					for (ClientInfo ci : ServerApplication.clients.values()) {
						ci.getOos().writeObject(tweet);
						ci.getOos().flush();
					}
				}
				
			}
		} catch (IOException e) {

			try {
				socket.close();
				ois.close();
				oos.close();
				ServerApplication.clients.remove(userId);
				System.out.println("Fechei a socket");
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
