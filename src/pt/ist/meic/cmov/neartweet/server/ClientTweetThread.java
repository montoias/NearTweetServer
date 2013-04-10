package pt.ist.meic.cmov.neartweet.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ist.meic.cmov.neartweet.dto.TweetDto;


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
				String id = tweet.getTweetId();
				boolean privacy = tweet.getPrivacy();

				if (tweet.getConversationID().equalsIgnoreCase("-1"))
					tweet.setConversationID(id);

				if (privacy) {

					System.out.println(tweet.getReceivingEntities().toString()
							+ " " + tweet.getTweet() + " id:"
							+ tweet.getTweetId() + "conversation id:"
							+ tweet.getConversationID());
					
					boolean anyEntityIsValid = tweetDestiny(tweet);
					if (!anyEntityIsValid)
						System.out.println("there is NO valid entitys");
					else
						System.out.println("there IS valid entitys");

				} else {
					for (ClientInfo ci : ServerApplication.clients.values()) {
						ci.getOos().writeObject(tweet);
						ci.getOos().flush();
					}
					
					System.out.println("Broadcast" + tweet.getTweet() + " id:"
							+ tweet.getTweetId() + "conversation id:"
							+ tweet.getConversationID());

				}

			}
		} catch (IOException e) {
			closeConnection();	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			
	}

	private void closeConnection() {
		try {
			socket.close();
			ois.close();
			oos.close();
			ServerApplication.clients.remove(userId);
			System.out.println("Fechei a socket");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	private boolean tweetDestiny(TweetDto tweet) throws IOException {
		boolean anyEntityIsValid = false;
		for (String eachReceiver : tweet.getReceivingEntities()) {
			String user = eachReceiver.replace("@", "");
			if (ServerApplication.clients.containsKey(user)) {
				ClientInfo ci = ServerApplication.clients.get(user);
				ci.getOos().writeObject(tweet);
				ci.getOos().flush();
				System.out.println("To: " + user);
				anyEntityIsValid = true;
			}
		}
		return anyEntityIsValid;
	}

}
