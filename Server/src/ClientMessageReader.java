import java.io.BufferedReader;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

/**
 * This class is used accept the message from multiple clients.
 */


public class ClientMessageReader extends Thread
{
	private BufferedReader reader; 
	private BlockingQueue<Message> messageQueue;
	
	public ClientMessageReader(BufferedReader reader, BlockingQueue<Message> messageQueue) {
		this.reader = reader;
		this.messageQueue = messageQueue;
	}
	
	@Override
	//This thread reads messages from the client's socket input stream
	public void run() {
		try {
			
			System.out.println(Thread.currentThread().getName() 
					+ " - Reading messages from client connection");
			
			String clientMsg = null;
			while ((clientMsg = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() 
						+ " - Message from client received: " + clientMsg);
				//place the message in the queue for the client connection thread to process
				Message msg = new Message(true, clientMsg);
				messageQueue.add(msg);
			}
			Message exit = new Message(false, "exit");
			messageQueue.add(exit);
			reader.close();
			
		} catch (SocketException e) {

			Message exit = new Message(false, "exit");
			messageQueue.add(exit);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
