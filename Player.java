/*
 * Equivalent of client
 * Will store: card on top of deck, their own hand, how many cards the opponent has, their own name
*/

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
 
public class Player {
	private ArrayList<Card> hand = new ArrayList<Card>();
	private static Card onStack;
	private Socket sock;
	private String name;
	private UnoGUI gui;
	private ObjectOutputStream objectToServer;
	private ObjectInputStream objectFromServer;
	private int opponent; // number of cards held by the opponent
  
	public Player (String host, int port, String name, UnoGUI gui) {
		this.name = name;
		this.gui = gui;
		  
		try {
			sock = new Socket(host, port);
 
			// write TO the server
			objectToServer = new ObjectOutputStream(sock.getOutputStream());
		 
		}
		catch (Exception e) {
			// error pop-up/message
			JFrame error = new JFrame();
			JOptionPane.showMessageDialog(error, "Failed to Connect", "Connection Error", JOptionPane.ERROR_MESSAGE);
			error.setVisible(true);
		}

		// at the start of the game, both players will have 7 cards
		opponent = 7;
  
		System.out.println("PLAYER SUCCESSFULLY CREATED");

		// start listening to the server
		(new listeningThread(sock)).start();
	}

	// player objects created server-side
	public Player(Socket sock) {
		this.sock = sock;

		try {
			// write TO the player
			objectToServer = new ObjectOutputStream(sock.getOutputStream());
		}
		catch (Exception e) { }
	}

	public Socket getSocket() {
		return sock;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public int getHandSize() {
		return hand.size();
	}
  
	public synchronized void drawCard() {
		Card temp = new Card(-5, 's'); // random, unimportant arguments here
		temp.setStatus("request"); // the important argument
		 
		try {
			objectToServer.writeObject(temp);
			objectToServer.flush();
		} catch (IOException e) {
			// error message
		}

		printHand();
	}

	public void updateStack(Card card) {
		onStack = card;
		char c = card.getCol();
		int n = card.getNum();
		gui.updateStack(n + " " + c);
		// send to the gui
	}

	public void remove(int number, char color) {
		for (int i = 0; i < hand.size(); i++) {
			int num = hand.get(i).getNum();
			char col = hand.get(i).getCol();
 
			if (num == number && col == color) {
				hand.remove(i);
				break;
			}
		}
	}

	public void disconnect() {
		try {
			objectToServer.close();
			objectFromServer.close();
			sock.close();
		}
		catch (Exception e) {}
	}

	public Card wildCard() {
		String [] options = {"red", "yellow", "blue", "green"};
		int choice = JOptionPane.showOptionDialog(new JFrame(), "Change to which color?", "Wild Card!", 0, 3, null, options, options[0]);
		Card toSend = new Card(500, 'r');

		if (choice == 0) {
			toSend = new Card(500, 'r');
			toSend.setStatus("played");
			return toSend;
		}

		if (choice == 1) {
			toSend = new Card(500, 'y');
			toSend.setStatus("played");
			return toSend;
		}

		if (choice == 2) {
			toSend = new Card(500, 'b');
			toSend.setStatus("played");
			return toSend;
		}

		if (choice == 3) {
			toSend = new Card(500, 'g');
			toSend.setStatus("played");
			return toSend;
		}

		return toSend;	
	}

	// triggered by the GUI, play a card
	public boolean playCard(int num, char color) {
		gui.updatePlayer("Trying to play: " + num + " " + color);

		Card card = new Card(num, color);
		card.setStatus("played");

		// if it's the first turn
		if (onStack == null) {
			// if it's a wild card
			if (color == 's') {
				Card toSend = wildCard();
				
				try {
					objectToServer.writeObject(toSend);
					objectToServer.flush();
				} catch (IOException e) {
					System.out.println("FAILED TO PLAY FIRST TURN");
				}

				// update stack
				updateStack(toSend);
				
				gui.updatePlayer("PLAYED FIRST TURN: " + toSend.getNum() + " " + toSend.getCol());
				
			}
			else {
				// normal card
				try {
					objectToServer.writeObject(card);
				objectToServer.flush();
				} catch (IOException e) {
					System.out.println("FAILED TO PLAY FIRST TURN");
				}

				// update stack
				updateStack(card);

				gui.updatePlayer("PLAYED FIRST TURN: " + num + " " + color);
			}

			// remove from hand
			remove(num, color);

			printHand();

			return true;
		}

		// wild cards always get played
		if (color == 's') {
			Card toSend = wildCard();

			// send to the server
			try {
				objectToServer.writeObject(toSend);
				objectToServer.flush();
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY WILD CARD");
			}
 
			// remove from hand
			remove(num, color);

			// update stack
			updateStack(toSend);

			gui.updatePlayer("PLAYED WILD CARD: " + toSend.getNum() + " " + toSend.getCol());
			printHand();
 
			return true;
		}
  
		// if the colors match
		else if (color == onStack.getCol()) {
			try {
				objectToServer.writeObject(card);
				objectToServer.flush();
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY MATCHING COLOR CARD");
			}
 
			// remove from hand
			remove(num, color);

			// update stack
			updateStack(card);

			gui.updatePlayer("PLAYED MATCHING COLOR CARD: " + num + " " + color);
			printHand();
 
			return true;
		}
  
		// if the numbers match
		else if (num == onStack.getNum()) {
			// send to the server
			try {
				objectToServer.writeObject(card);
				objectToServer.flush();
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY MATCHING NUMBER CARD");
			}
 
			// remove from hand
			remove(num, color);

			// update stack
			updateStack(card);
			
			gui.updatePlayer("PLAYED MATCHING NUMBER CARD: " + num + " " + color);
			printHand();

			return true;
		}
  
		else {
			gui.updatePlayer("CARD COULD NOT BE PLAYED -- NO ERROR BESIDES PLAYER JUDGEMENT");
			// pop-up telling the player that card isn't playable?
			return false;
		}
	}

	// used server-side to send things to players client-side
	public void playCard(Card card) {
		try {
			objectToServer.writeObject(card);
			objectToServer.flush();
		} catch (IOException e) {
			System.out.println("FAILED TO PLAY CARD");
		}
	}

	// used to differentiate the types of cards the client can recieve from the server
	public void receiveCard(Card card) {
		// drew a card--add it to the hand
		if (card.getStatus().equals("drawn")) {
			hand.add(card);
			gui.updatePlayer("Drew card: " + card.getNum() + " " + card.getCol());
			// send to the gui--send the whole array
			// gui.updateHand(hand);
		}
		// opponent has drawn a card--update their hand count
		else if (card.getStatus().equals("update")) {
			opponent++;
			gui.updateOpponent("OPPONENT HAS: " + opponent + " cards");
			// tell the gui!
			
		}
		// opponent played a card--update stack and do whatever else may be required
		else if (card.getStatus().equals("played")) {
			// update stack
			updateStack(card);

			// update opponent
			opponent--;
			gui.updateOpponent("OPPONENT HAS: " + opponent + " cards");

			// draw 4
			if (card.getNum() == 400) {
				drawCard();
				drawCard();
				drawCard();
				drawCard();
				gui.updateOpponent("OPPONENT PLAYED DRAW 4");
			}
			// draw 2
			else if (card.getNum() == 200) {
				drawCard();
				drawCard();
				gui.updateOpponent("OPPONENT PLAYED DRAW 2");
			}
			// skip turn
			else if (card.getNum() == 300) {
				// do nothing...?
				gui.updateOpponent("OPPONENT PLAYED SKIP");
			}
			// rotate
			else if (card.getNum() == 100) {
				// again, do nothing
				gui.updateOpponent("OPPONENT PLAYED ROTATE");
			}
		}
	}

	// thoughts on UNO button: we need to listen to it from both sides, so what if it's a static object across all guis?
	// then thread it in the server (idk how it gets to the server, maybe the player will send it) so it exists on both guis
	// as the same button with the same thread?
  
	// listens for Card objects from the server
	private class listeningThread extends Thread {
		private Socket sock;
		  
		public listeningThread (Socket sock) {
			this.sock = sock;
 
			// set up the i/o stream readers
			try {
				// read FROM the server
				objectFromServer = new ObjectInputStream(sock.getInputStream());
			}
			catch (Exception e) {
				System.out.println("FAILED TO START LISTENING THREAD");
			}
			
			System.out.println("STARTED LISTENING THREAD");

		}
 
		@Override
		public void run() {
			try {
				while(true) {
					System.out.println("Waiting for card...");
					Card card = (Card) objectFromServer.readObject();
					receiveCard(card);
				}
			}
			catch (Exception e) {
				System.out.println("DISCONNECTED");
				e.printStackTrace();
			}
		}
	} 

	public void printHand() {
		gui.clearHand();

		for (int i = 0; i < hand.size(); i++) {
			gui.updateHand(hand.get(i).getNum() + " " + hand.get(i).getCol());
		}
	}
 
	public static void main(String[] args) {
		// TESTING--THIS WILL BE DELETED
		Player player = new Player("localhost", 8181, "TESTER", null);

		player.playCard(1, 'r'); // first turn check
		player.playCard(1, 'y'); // matching number check
		player.playCard(5, 'y'); // matching color check
		player.playCard(500, 's'); // wild card check

	}
}
 
