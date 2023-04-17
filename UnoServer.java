import java.io.*;
import java.net.*;
import java.util.*;

/*
 * This is the server
 * Stores player objects, stack of cards played (only the top one is important), the available cards to pull, base deck
 */

public class UnoServer {
	private ArrayList<Card> baseDeck = new ArrayList<Card>();
	private ArrayList<Card> deckInPlay = new ArrayList<Card>();

	private ServerSocket server;
	
	public UnoServer (int port) {
		try {
			server = new ServerSocket(port);
			// populate the deck?
		}
		catch (Exception e) {
			// error pop-up/message
			System.exit(1);
		}
	}

	// listen for incoming connections
	public void serve() {
		while(true){
			try{
				System.out.println("Waiting for connections...");
				
				// accept incoming connection
				Socket newPlayer = server.accept();

				System.out.println("Accepted");

				// do whatever it is you need to do here--honestly, not sure yet
				// certainly must send some info to the player...

				// start listening to the player
				(new readingThread(newPlayer)).start();
			}
			catch(Exception e){
				// error pop-up/message
				System.exit(1);
			}
		}
	}

	// listen for communication from clients, new thread for each client
	private class readingThread extends Thread {
		private Socket sock;

		public readingThread(Socket sock) {
			this.sock = sock;
			// set up i/o stream readers
			try {

			}
			catch (Exception e) {
				// error pop-up/message
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					// the things it does with the info from the player
				}
			}
			catch (Exception e) {
				// error pop-up/message
			}
		}
	}

}
