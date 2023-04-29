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
	private int plusCards;
  
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
	}

	public void multiDrawCard(int num) {
		while (num != 0) {
			drawCard();
			num--;
		}
	}

	public void updateStack(Card card) {
		onStack = card;
		char c = card.getCol();
		int n = card.getNum();
		gui.updateStack(n + " " + c);
		// gui.updateStack(card);
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
		gui.updatePlayer("Trying to play: " + num + " " + color);

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
					System.out.println("FAILED TO PLAY FIRST TURN");
				}

				// update stack
				updateStack(toSend);

				if (num == 400) {
					plusCards += 4;
				}
				
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

				if (num == 200) {
					plusCards += 2;
				}

				gui.updatePlayer("PLAYED FIRST TURN: " + num + " " + color);
			}

			// remove from hand
			remove(num, color);

			printHand();

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
					System.out.println("FAILED TO PLAY MATCHING NUMBER CARD");
				}
		
				// remove from hand
				remove(num, color);
		
				// update stack
				updateStack(card);

				plusCards += 2;
			
				gui.updatePlayer("PLAYED MATCHING NUMBER CARD: " + num + " " + color);
				printHand();
		
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
					System.out.println("FAILED TO PLAY WILD CARD");
				}
 
				// remove from hand
				remove(num, color);

				// update stack
				updateStack(toSend);

				plusCards += 4;

				gui.updatePlayer("PLAYED WILD CARD: " + toSend.getNum() + " " + toSend.getCol());
				printHand();
 
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
			updateStack(toSend);

			if (num == 400) {
				plusCards += 4;
			}

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

			if (num == 200) {
				plusCards += 2;
			}

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
			printHand(); // this would be sending the hand to the GUI

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


		// If it recieves Uno Card, plays UNO GUI
		if (card.getStatus().equals("Uno!")) {
		
			Thread unoPopup = new UnoPopup('U', 1);
        		unoPopup.start();
	
		}
		
		// If it recieves the Fail Uno Card, it plays the Fail Uno GUI
		if (card.getStatus().equals("Uno?")) {
			
		        Thread unoPopup = new UnoPopup('U', 2);
	        	unoPopup.start();
		}


		if (card.getStatus().equals("drawn")) {
			hand.add(card);
			gui.updatePlayer("Drew card: " + card.getNum() + " " + card.getCol());
			printHand();
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
				gui.updateOpponent("OPPONENT PLAYED DRAW 4");
				plusCards += 4;

				gui.updateOpponent("Will have to draw " + plusCards + " cards");
			}

			// draw 2
			else if (card.getNum() == 200) {
				gui.updateOpponent("OPPONENT PLAYED DRAW 2");
				plusCards += 2;

				gui.updateOpponent("Will have to draw " + plusCards + " cards");
			}
			

			// skip turn
			if (card.getNum() == 300) {
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
  
	// When UNO Button is Pressed
	// If this player has UNO, then "Uno!" is send to server. If not, then "Uno?" is sent to server
 	public void uno() {

		// Creates a Fake Card to send Message to Server
		Card uno;
		Thread unoPopup;

 		// Checks if the player has UNO
 		if (getHandSize() == 1) {		
			// Sets the Card's Status to "UNO!"
			uno = new Card (1, 'U');
			uno.setStatus("Uno!");
			unoPopup = new UnoPopup('U', 1);
		} else {
			// Sets the Card's Status to "UNO?"
			uno = new Card (2, 'U');
			uno.setStatus("Uno?");
			unoPopup = new UnoPopup('U', 2);
		}

		unoPopup.start();

                // Sends the Card to the Server
                try {   
                         objectToServer.writeObject(uno);
                         objectToServer.flush();
                } catch (IOException e) {
                         System.out.println("UNO FAILURE");
                }

 		// If This player doesn't have Uno, Sends UNO Message to Server and Server Determines if the other player has UNO
 		// If noone has UNO, then this player picks two cards. If the other player has UNO, then they pick two cards
 	}

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
					System.out.println("May potentially need to draw " + plusCards + " cards");
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
 
