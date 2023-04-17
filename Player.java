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
	 public void playCard(Card card) {
		 int cardNum = card.getNumber();
		 char cardCol = card.getColor();
 
		 // wild cards always get played
		 if (cardCol == 's') {
			 // send to the server
		 }
 
		 // if the colors match
		 else if (cardCol == onStack.getColor()) {
			 // send to the server
		 }
 
		 // if the numbers match
		 else if (cardNum == onStack.getNumber()) {
			 // send to the server
		 }
 
		 else {
			 // pop-up telling the player that card isn't playable?
		 }
	 }
   
   // Or, since the GUI already has access to the ArrayList in order to present it, it can just send the number in the array that is clicked?
	public void playCard(int played) {
		if (canPlay(hand.get(played)) == true) {
			// Sends the Card to the Network (Recieved back from Network to update top of Stack)
		} else {
			// GUI popup says "You can't do that!!!"
		}
	}
 
	 public void receiveCard(Card card) {
		 hand.add(card);
	 }
   
   // Updates the Stack with the Card Recieved. 
	public void updateStack(Card card) {
		onStack = card;
	}
  
  	// If they press the Uno Button on the GUI
	public void uno() {
		if (hand.size() == 1) {
			// Reflected on GUI
		} else {
			// Wah Wah
		}
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
