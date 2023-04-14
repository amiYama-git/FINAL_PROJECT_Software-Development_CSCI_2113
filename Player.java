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

	}

	public Card drawCard() {
		return null;
	}

	public void playCard(int number, char color) {
		// check color first
	}

	public void receiveCard(Card card) {

	}


	
	
}
