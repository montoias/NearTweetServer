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

				String[] splitedTweet = tweet.getTweet().split(" ");

				for (String eachSplit : splitedTweet) {
					if (eachSplit.contains("@")) {
						tweet.getReceivingEntities().add(eachSplit);
					}
				}

				System.out.println(tweet.getReceivingEntities().toString() + " "
						+ tweet.getTweet() + " id:" + tweet.getTweetId()
						+ "conversation id:" + tweet.getConversationID());

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

				if (!anyEntityIsValid) {
					System.out.println("BroadCast");
					for (ClientInfo ci : ServerApplication.clients.values()) {
						ci.getOos().writeObject(tweet);
						ci.getOos().flush();
					}
				}
				
				// Send to the sender
				if(ServerApplication.clients.containsKey(tweet.getSender()) && anyEntityIsValid){
					ClientInfo ci = ServerApplication.clients.get(tweet.getSender());
					ci.getOos().writeObject(tweet);
					System.out.println("To: " + tweet.getSender());
					ci.getOos().flush();
					
				}
			}
		} catch (IOException e) {
			// Do Nothing

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	}

}
