import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {

		Gpio pins = new Gpio();

		ServerSocket listener = new ServerSocket(9090);
		System.out.println("Starting server");

		while(true) {
			Socket socket = listener.accept();

			InputStreamReader reader = new InputStreamReader(socket.getInputStream());
			BufferedReader input = new BufferedReader(reader);

			String signal = input.readLine();
			System.out.println(signal);
			
			switch (signal) {
			case "TOGGLE":
				String string = input.readLine();
				pins.toggle(string);
				break;
			case "TIMER":
				pins.setTimer(input);
				break;
			case "REMOVE":
				pins.removeTimer(input);
				break;
			case "INITIATE":
				getWriter(socket).println(pins.getCurrentState());
				System.out.println("Current state: " + pins.getCurrentState());
				break;
			default:
				break;
			}
			
			socket.close();
		}
	}

	private static PrintWriter getWriter(Socket socket) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}
}