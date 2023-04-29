/*
 * Equivalent of client
 * Will store: card on top of deck, their own hand, how many cards the opponent has, their own name
 * 
 * NEED A draw button, way to delete cards from the hand when they're played
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
	private UnoScreen gui;
	private ObjectOutputStream objectToServer;
	private ObjectInputStream objectFromServer;
	private int opponent; // number of cards held by the opponent
	private int plusCards;
  
	public Player (String host, int port, String name, UnoScreen gui) {
		this.name = name;
		this.gui = gui;
		  
		try {
			sock = new Socket(host, port);
 
			// write TO the server
			objectToServer = new ObjectOutputStream(sock.getOutputStream());
		 
		}
		catch (Exception e) {
			// error pop-up/message
			UnoGUI.error("Failed to connect");
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
			UnoGUI.error("Failed to draw card");
		}
	}

	public void multiDrawCard(int num) {
		while (num != 0) {
			drawCard();
			num--;
		}
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

	// for making wild cards
	public Card wildCard(int num) {
		String [] options = {"red", "yellow", "blue", "green"};
		int choice = JOptionPane.showOptionDialog(new JFrame(), "Change to which color?", "Wild Card!", 0, 3, null, options, options[0]);
		Card toSend = new Card(num, 'r'); // the default

		if (choice == 0) {
			toSend = new Card(num, 'r');
			toSend.setStatus("played");
			return toSend;
		}

		if (choice == 1) {
			toSend = new Card(num, 'y');
			toSend.setStatus("played");
			return toSend;
		}

		if (choice == 2) {
			toSend = new Card(num, 'b');
			toSend.setStatus("played");
			return toSend;
		}

		if (choice == 3) {
			toSend = new Card(num, 'g');
			toSend.setStatus("played");
			return toSend;
		}

		return toSend;	
	}

	// triggered by the GUI, play a card
	public boolean playCard(int num, char color) {
		Card card = new Card(num, color);
		card.setStatus("played");

		// if it's the first turn
		if (onStack == null) {
			// if it's a wild card
			if (color == 's') {
				Card toSend = wildCard(num);
				
				try {
					objectToServer.writeObject(toSend);
					objectToServer.flush();
				} catch (IOException e) {
					UnoGUI.error("FAILED TO PLAY FIRST TURN");
				}

				// update stack
				onStack = toSend;
				gui.updateStack(card);

				if (num == 400) {
					plusCards += 4;
				}
			}
			else {
				// normal card
				try {
					objectToServer.writeObject(card);
					objectToServer.flush();
				} catch (IOException e) {
					UnoGUI.error("FAILED TO PLAY FIRST TURN");
				}

				// update stack
				onStack = card;
				gui.updateStack(card);

				if (num == 200) {
					plusCards += 2;
				}
			}

			// remove from hand
			remove(num, color);
			gui.cardarray(hand);

			return true;
		}

		// potentially stacking on +# cards
		if (onStack.getNum() == 200 || onStack.getNum() == 400) {
			// stack with another +2
			if (num == 200 && color == onStack.getCol()) {
				// send the card as usual
				try {
					objectToServer.writeObject(card);
					objectToServer.flush();
				} catch (IOException e) {
					UnoGUI.error("FAILED TO PLAY MATCHING NUMBER CARD");
				}
		
				// update stack
				onStack = card;
				gui.updateStack(card);

				plusCards += 2;

				// remove from hand
				remove(num, color);
				gui.cardarray(hand);
		
				return true;
			}

			// stack with a +4
			else if (num == 400) {
				Card toSend = wildCard(num);

				// send to the server
				try {
					objectToServer.writeObject(toSend);
					objectToServer.flush();
				} catch (IOException e) {
					UnoGUI.error("FAILED TO PLAY WILD CARD");
				}
 
				// remove from hand
				remove(num, color);

				// update stack
				onStack = toSend;
				gui.updateStack(card);

				plusCards += 4;

				gui.cardarray(hand);
 
				return true;
			}

			// don't stack, therefore must draw cards; then goes on to play whatever card
			else {
				multiDrawCard(plusCards);
				plusCards = 0; // reset
			}
		}

		// wild cards always get played
		if (color == 's') {
			Card toSend = wildCard(num);

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
			onStack = toSend;
			gui.updateStack(card);

			if (num == 400) {
				plusCards += 4;
			}

			gui.cardarray(hand);
 
			return true;
		}
  
		// if the colors match
		else if (color == onStack.getCol()) {
			try {
				objectToServer.writeObject(card);
				objectToServer.flush();
			} catch (IOException e) {
				UnoGUI.error("FAILED TO PLAY MATCHING COLOR CARD");
			}
 
			// remove from hand
			remove(num, color);

			// update stack
			onStack = card;
			gui.updateStack(card);

			if (num == 200) {
				plusCards += 2;
			}

			gui.cardarray(hand);
 
			return true;
		}
  
		// if the numbers match
		else if (num == onStack.getNum()) {
			// send to the server
			try {
				objectToServer.writeObject(card);
				objectToServer.flush();
			} catch (IOException e) {
				UnoGUI.error("FAILED TO PLAY MATCHING NUMBER CARD");
			}
 
			// remove from hand
			remove(num, color);

			// update stack
			onStack = card;
			gui.updateStack(card);
			
			gui.cardarray(hand);

			return true;
		}
  
		else {
			UnoGUI.error("CARD CANNOT BE PLAYED");
			return false;
		}
	}

	// used server-side to send things to players client-side
	public void playCard(Card card) {
		try {
			objectToServer.writeObject(card);
			objectToServer.flush();
		} catch (IOException e) {
			UnoGUI.error("FAILED TO PLAY CARD SERVERSIDE");
		}
	}

	// used to differentiate the types of cards the client can recieve from the server
	public void receiveCard(Card card) {
		// drew a card--add it to the hand
		if (card.getStatus().equals("drawn")) {
			hand.add(card);
			gui.cardarray(hand);
		}
		// opponent has drawn a card--update their hand count
		else if (card.getStatus().equals("update")) {
			opponent++;
			gui.updateOpponent(opponent);
		}
		// opponent played a card--update stack and do whatever else may be required
		else if (card.getStatus().equals("played")) {
			// update stack
			onStack = card;

			if (card.getNum() == 400) {
				gui.updateStack(new Card(400, 's'));
			}
			else if (card.getNum() == 500) {
				gui.updateStack(new Card(500, 's'));
			}
			else {
				gui.updateStack(card);
			}

			// update opponent
			opponent--;
			gui.updateOpponent(opponent);

			
			// draw 4
			if (card.getNum() == 400) {
				plusCards += 4;
			}

			// draw 2
			else if (card.getNum() == 200) {
				plusCards += 2;
			}

			// skip turn
			if (card.getNum() == 300) {
				// do nothing...?
				UnoGUI.error("TURN SKIPPED!");
			}
			
			// rotate
			else if (card.getNum() == 100) {
				// again, do nothing
				UnoGUI.error("ROTATE!");
			}
		}
	}

	// thoughts on UNO button: we need to listen to it from both sides, so what if it's a static object across all guis?
	// then thread it in the server (idk how it gets to the server, maybe the player will send it) so it exists on both guis
	// as the same button with the same thread?
  
	// listens for Card objects from the server
	private class listeningThread extends Thread {
		private Socket sock;
		private boolean firstTurn = true;
		  
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
					if (firstTurn && hand.size() == 7) {
						gui.updateOpponent(opponent);
						firstTurn = false;
					}

					System.out.println("Waiting for card...");
					System.out.println("May potentially need to draw " + plusCards + " cards");
					Card card = (Card) objectFromServer.readObject();
					receiveCard(card);
				}
			}
			catch (Exception e) {
				System.out.println("DISCONNECTED");
				System.exit(1);

			}
		}
	}
}
 
