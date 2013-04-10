package pt.ist.meic.cmov.neartweet.server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientInfo {

	Socket socket;	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	String userName;


	public ClientInfo(ObjectInputStream _ois, ObjectOutputStream _oos, Socket _socket) {
		ois = _ois;
		oos = _oos;
		socket = _socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
