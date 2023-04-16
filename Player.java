/*
 * Equivalent of client
 * Will store: card on top of deck, their own hand, how many cards the opponent has, their own name
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Player {
	private ArrayList<Card> hand = new ArrayList<Card>();
	static Card onStack;
	

	public Player (String host, int port, String name, UnoGUI gui) {
		// Connects to the Server
		
		// Starts thread that checks for updates to stack and cards recieved from the server
		Thread t = new ServerThread();
		t.start();
	}

	// Thread that runs as long as the game is going. Checks for cards from server
	private class ServerThread extends Thread {
		public void run() {
			// Distinguishes between Cards going to stack and cards going to hand and puts them in the methods below
			updateStack( /* Card */ );
			recieveCard( /* Card */ );
		}
	}
	
	// Maybe void, so that it just prints a request to the Server, and then when the server sends the card, it goes straight to the hand via the recieveCard Method like other Recieved Cards and is updates the same way?
	public Card drawCard() {
		// Prints Request to Server
		return null;
	}
	
	// Option 1
	public void playCard(int number, char color) {
		// check color first
	}
	
	// Or, since the GUI already has access to the ArrayList in order to present it, it can just send the number in the array that is clicked?
	public void playCard(int played) {
		if (canPlay(hand.get(played)) == true) {
			// Sends the Card to the Network (Recieved back from Network to update top of Stack)
		} else {
			// GUI popup says "You can't do that!!!"
		}
	}

	// Compares a card to the top of the stack to see if you are able to play it. 
	private Boolean canPlay (Card card) {
		// If the color is special, the color matches the top of the stack, or the number matches the top of the stack, then it can be played. 
		if (card.getCol() == 's' || card.getCol() == onStack.getCol() || card.getNum() == onStack.getNum()) {
			return true;
		}
		// If it doesn't then it can not. 
		else {
			return false;
		}
			
	}
	
	// Updates the Stack with the Card Recieved. 
	public void updateStack(Card card) {
		onStack = card;
	}
	
	// Adds the Card Recieved to the hand. 
	public void receiveCard(Card card) {
		hand.add(card);
	}

	// If they press the Uno Button on the GUI
	public void uno() {
		if (hand.size() == 1) {
			// Reflected on GUI
		} else {
			// Wah Wah
		}
	}
	
	
}
