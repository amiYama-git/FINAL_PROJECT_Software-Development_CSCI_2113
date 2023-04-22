/*
 * Equivalent of client
 * Will store: card on top of deck, their own hand, how many cards the opponent has, their own name
*/

import java.io.*;
import java.net.*;
import java.time.Year;
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
	private static ArrayList<Player> players = new ArrayList<>();
  
	public Player (String host, int port, String name, UnoGUI gui) {
		players.add(this);
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
  
		// start listening to the server
		(new listeningThread(sock)).start();
 
		System.out.println("PLAYER SUCCESSFULLY CREATED");
	}
 
	
	public static Player getPlayer(Socket sock) {
		Iterator<Player> iterator = players.iterator();

		while (iterator.hasNext()) {
			Player temp = iterator.next();

			if (temp.getSocket().equals(sock)) {
				return temp;
			}
		}

		return null;
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

	public void updateHands() {
		for (int i = 0; i < players.size(); i++) {
			// send the gui the hand size
			players.get(i).getHandSize();

		}
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

	public void updateStack(Card card) {
		onStack = card;
		char c = card.getCol();
		int n = card.getNum();
		System.out.println("UPDATED STACK WITH: " + c + " " + n);
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

	// triggered by the GUI
	public boolean playCard(int num, char color) {
		System.out.println("Trying to play: " + num + " " + color);

		Card card = new Card(num, color);
		card.setStatus("played");

		// if it's the first turn
		if (onStack == null) {
			try {
				objectToServer.writeObject(card);
				objectToServer.flush();
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY FIRST TURN");
			}

			// update stack
			onStack = card;

			// remove from hand
			remove(num, color);

			System.out.println("PLAYED FIRST TURN: " + num + " " + color);

			return true;
		}

		System.out.println("On stack is: " + onStack.getNum() + " " + onStack.getCol());


		// wild cards always get played
		if (color == 's') {
			/* ask for what color to change it to -- trigger some GUI thing
			String [] options = {"red", "yellow", "blue", "green"};
			JOptionPane.showOptionDialog(new JFrame(), "Change to which color?", "Wild Card!", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 'r');
			*/

			// send to the server
			try {
				objectToServer.writeObject(card);
				objectToServer.flush();
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY WILD CARD");
			}
 
			// remove from hand
			remove(num, color);

			// update stack
			onStack = card;

			System.out.println("PLAYED WILD CARD");
 
			return true;
		}
  
		// if the colors match ************this is not working
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
			onStack = card;

			System.out.println("PLAYED MATCHING COLOR CARD: " + num + " " + color);
 
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
			onStack = card;
			
			System.out.println("PLAYED MATCHING NUMBER CARD: " + num + " " + color);

			return true;
		}
  
		else {
			System.out.println("CARD COULD NOT BE PLAYED -- NO ERROR BESIDES PLAYER JUDGEMENT");
			// pop-up telling the player that card isn't playable?
			return false;
		}
	}

	public void receiveCard(Card card) {
		// drew a card--add it to the hand
		if (card.getStatus().equals("drawn")) {
			hand.add(card);
			System.out.println("Drew card: " + card.getCol() + " " + card.getNum());
			// send to the gui--send the whole array
			// gui.updateHand(hand);
		}
		// opponent has done something--update their hand count
		else if (card.getStatus().equals("update")) {
			opponent = card.getNum();
			System.out.println("OPPONENT HAS: " + opponent + " cards");
			// tell the gui!
			
		}
		// opponent played a card--update stack and do whatever else may be required
		else if (card.getStatus().equals("played")) {
			// update stack

			updateStack(card);
			// draw 4
			if (card.getNum() == 400) {
				drawCard();
				drawCard();
				drawCard();
				drawCard();
				System.out.println("OPPONENT PLAYED DRAW 4");
			}
			// draw 2
			else if (card.getNum() == 200) {
				drawCard();
				drawCard();
				System.out.println("OPPONENT PLAYED DRAW 2");
			}
			// skip turn
			else if (card.getNum() == 300) {
				// do nothing...?
				System.out.println("OPPONENT PLAYED SKIP");
			}
			// rotate
			else if (card.getNum() == 100) {
				// again, do nothing
				System.out.println("OPPONENT PLAYED ROTATE");
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
  
		}
 
		@Override
		public void run() {
			try {
				while(true) {
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
 
	public static void main(String[] args) {
		// TESTING--THIS WILL BE DELETED
		Player player = new Player("localhost", 8181, "TESTER", null);

		player.playCard(1, 'r'); // first turn check
		player.playCard(1, 'y'); // matching number check
		player.playCard(5, 'y'); // matching color check
		player.playCard(500, 's'); // wild card check

	}
}
 
