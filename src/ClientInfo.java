import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientInfo {

	Socket socket;	
	ObjectInputStream ois;
	ObjectOutputStream oos;


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

	
}
