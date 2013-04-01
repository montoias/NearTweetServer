import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dto.ResponseDto;
import dto.UserNameDto;

public class RegisterThread extends Thread {

	public void run() {

		Socket clientSocket;
		String userId;

		while (true) {
			try {
				clientSocket = ServerApplication.serverSocket.accept();
				System.out.println("Client Wants to register! " + clientSocket.getInetAddress().getHostAddress() );

				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				UserNameDto user = (UserNameDto) ois.readObject();
				userId = user.getInput();

				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ResponseDto respDto = new ResponseDto();

				if (ServerApplication.clients.containsKey(userId)) {
					respDto.setResponse("NOK");
					System.out.println("NOK");
					oos.writeObject(respDto);
					oos.flush();
					throw new RuntimeException("Client Already exists");
				} else {
					respDto.setResponse("OK");
					System.out.println("OK");
					oos.writeObject(respDto);
					oos.flush();
				}

				System.out.println("Added to the clients list: " + userId);
				ServerApplication.clients.put(userId, new ClientInfo(ois, oos,
						clientSocket));

				new ClientTweetThread(userId).start();

			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
