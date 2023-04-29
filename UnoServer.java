import java.io.*;
import java.net.*;
import java.util.*;

/*
 * This is the server
 * Stores player objects, stack of cards played (only the top one is important), the available cards to pull, base deck
 */

public class UnoServer {
	private ServerSocket server;
	private Deck deckInPlay = new Deck();
	private PlayerHandler ph = new PlayerHandler();
	
	public UnoServer (int port) {
		try {
			server = new ServerSocket(port);
		}
		catch (Exception e) {
			System.out.println("FAILED TO MAKE SERVERSOCKET");
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
				
				// add to the player handler
				ph.add(newPlayer);

				// start listening to the player
				(new readingThread(newPlayer)).start();

				if (ph.players.size() == 2) {
					break;
				}
			}
			catch(Exception e){
				System.out.println("FAILED TO SERVE");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	// listen for communication from clients, new thread for each client
	private class readingThread extends Thread {
		private Socket sock;
		private ObjectInputStream objectFromPlayer;

		public readingThread(Socket sock) {
			System.out.println("ATTEMPTING TO CREATE READING THREAD");

			try {
				this.sock = sock;

				// read FROM the player
				objectFromPlayer = new ObjectInputStream(sock.getInputStream());
			}
			catch (Exception e) {
				System.out.println("FAILED TO START READING THREAD");
				// error pop-up/message
			}

			System.out.println("READING THREAD CREATED");
		}

		@Override
		public void run() {
			try {
				// deal the player 7 cards initially
				for (int i = 0; i < 7; i++) {
					System.out.println("DEALING CARD " + (i+1));
					Card temp = deckInPlay.drawCard();
					temp.setStatus("drawn");
					ph.dealCard(sock, temp);
				}
				
				while (true) {
					Card temp = (Card) objectFromPlayer.readObject();
					String status = temp.getStatus();

					if (status.equals("request")) {
						// player is requesting a card--send one
						Card toSend = deckInPlay.drawCard();
						toSend.setStatus("drawn");
						System.out.println("SENDING A DRAWN CARD");
						
						ph.dealCard(sock, toSend);

						// must tell opponent
						ph.updateOpponent(sock);
					}
					
					if (status.equals("played")) {
						// player has played a card--tell everyone to update their onStack Card
						// no changes needed
						System.out.println("SENDING A PLAYED CARD");
						ph.sendCard(sock, temp);
					}

						
					// If the Player has Uno
					if (status.equals("Uno!")) {
						// Tells Player Handler to Send all Players the UNO Card
						System.out.println("WE'VE GOT AN UNO");
						ph.sendCard(sock, temp);
					} 

					// If the Player is asking the Server whether others have an UNO
					if (status.equals("Uno?")) {
						// Lets the Player Handler Know, which checks for UNOs among the other players. 
						System.out.println("CHECKING FOR UNOS");
						ph.uno(sock, temp);
					}
				}
			}
			catch (Exception e) {
				UnoGUI.error("Player Disconnected");
				ph.remove(sock);
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		UnoServer game = new UnoServer(Integer.parseInt(args[0]));
		game.serve();
	}

}
