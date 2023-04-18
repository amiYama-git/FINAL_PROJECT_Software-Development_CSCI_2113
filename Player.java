/*
 * Equivalent of client
 * Will store: card on top of deck, their own hand, how many cards the opponent has, their own name
 */


import java.io.*;
import java.net.*;
import java.util.*;
 
public class Player {
	private ArrayList<Card> hand = new ArrayList<Card>();
	private static Card onStack;
	private Socket sock;
	private String name;
	private UnoGUI gui;
	private ObjectInputStream objectFromServer;
	private BufferedReader br;
	private ObjectOutputStream objectToServer;
	private PrintWriter pw;
 
	public Player (String host, int port, String name, UnoGUI gui) {
		this.name = name;
		this.gui = gui;
		 
		try {
			sock = new Socket(host, port);
		}
		catch (Exception e) {
			// error pop-up/message
		}
 
		// start listening to the server
		(new listeningThread(sock)).start();
	}
 
	public void drawCard() {
		// requests a card from the server
	}
 
	// triggered by the GUI
	public boolean playCard(int num, char color) {
		// wild cards always get played
		if (color == 's') {
			// check what kind of wild card it is
			// do the appropriate things
			// send to the server
			// remove from hand
			return true;
		}
 
		// if the colors match
		else if (color == onStack.getCol()) {
			// check to see if it's a +2, skip, or rotate
			// send to the server
			// remove from hand
			return true;
		}
 
		// if the numbers match
		else if (num == onStack.getNum()) {
			// send to the server
			// remove from hand
			return true;
		}
 
		else {
			// pop-up telling the player that card isn't playable?
			return false;
		}
	}

	 public void receiveCard(Card card) {
		hand.add(card);
		// send to the gui
	 }
   
	// Updates the Stack with the Card Recieved. 
	public void updateStack(Card card) {
		onStack = card;
	}
  
  	// If they press the Uno Button on the GUI
	public boolean uno() {
		if (hand.size() == 1) {
			// Reflected on GUI
			return true;
		} 
		// Wah Wah
		return False; 
	}
 
	private class listeningThread extends Thread {
		private Socket sock;
		private ObjectInputStream objectFromServer;
		private BufferedReader br;
		private ObjectOutputStream objectToServer;
		private PrintWriter pw;
		 
		public listeningThread (Socket sock) {
			this.sock = sock;
 
			// set up the i/o stream readers
			try {
				// read FROM the server
				objectFromServer = new ObjectInputStream(sock.getInputStream());
			 	br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
 
				// write TO the server
				objectToServer = new ObjectOutputStream(sock.getOutputStream());
				pw = new PrintWriter(sock.getOutputStream());
				 
			}
			catch (Exception e) {
				// error pop-up/message
			}
		}
 
		@Override
		public void run() {
			// whatever it does with the things the server is saying/sending
			try {
				while(true){ 
 
				}
			}
			catch (Exception e) {
				// error pop-up/message
			}
		}
	}
	 
}
