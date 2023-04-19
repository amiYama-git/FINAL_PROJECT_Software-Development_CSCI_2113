/*
 * Equivalent of client
 * Will store: card on top of deck, their own hand, how many cards the opponent has, their own name
 */


import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
public class Player {
	private ArrayList<Card> hand = new ArrayList<Card>();
	private static Card onStack;
	private Socket sock;
	private String name;
	private UnoGUI gui;
	private ObjectOutputStream objectToServer;
  
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
		}
  
		// start listening to the server
		(new listeningThread(sock)).start();
 
		System.out.println("PLAYER SUCCESSFULLY CREATED");
	}
 
	
	public Player (Socket sock) {
		this.sock = sock;
	}
 
	public Socket getSocket() {
		return sock;
	}
  
	public void drawCard() {
		System.out.println("ATTEMPTING TO DRAW CARD");
 
		Card temp = new Card(-5, 's'); // random, unimportant arguments here
		temp.setStatus("request"); // the important argument
		 
		try {
			objectToServer.writeObject(temp);
		} catch (IOException e) {
			// error message
		}
	}

	// triggered by the GUI
	public boolean playCard(int num, char color) {
		Card card = new Card(num, color);
		card.setStatus("played");
		// wild cards always get played
		if (color == 's') {
			/* ask for what color to change it to -- trigger some GUI thing
			String [] options = {"red", "yellow", "blue", "green"};
			JOptionPane.showOptionDialog(new JFrame(), "Change to which color?", "Wild Card!", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 'r');
			*/
			// send to the server
			try {
				objectToServer.writeObject(card);
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY WILD CARD");
			}
 
			// remove from hand
			remove(num, color);
 
			return true;
		}
  
		// if the colors match
		else if (color == onStack.getCol()) {
			 try {
				 objectToServer.writeObject(card);
			 } catch (IOException e) {
				 System.out.println("FAILED TO PLAY MATCHING COLOR CARD");
			 }
 
			 // remove from hand
			 remove(num, color);
 
			 return true;
		 }
  
		 // if the numbers match
		 else if (num == onStack.getNum()) {
			// send to the server
			try {
				objectToServer.writeObject(card);
			} catch (IOException e) {
				System.out.println("FAILED TO PLAY MATCHING NUMBER CARD");
			}
 
			// remove from hand
			remove(num, color);
 
			return true;
		}
  
		else {
			System.out.println("CARD COULD NOT BE PLAYED -- NO ERROR BESIDES PLAYER JUDGEMENT");
			// pop-up telling the player that card isn't playable?
			return false;
		}
	}
 
	public void receiveCard(Card card) {
		hand.add(card);
		System.out.println("Received card: " + card.getCol() + " " + card.getNum());
		// send to the gui--what is the method called?
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
   
	// thoughts on UNO button: we need to listen to it from both sides, so what if it's a static object across all guis?
	// then thread it in the server (idk how it gets to the server, maybe the player will send it) so it exists on both guis
	// as the same button with the same thread?
  
	// listens for Card objects from the server
	private class listeningThread extends Thread {
		private Socket sock;
		private ObjectInputStream objectFromServer;
		  
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
					
					if (card.getStatus().equals("drawn")) {
						// add to hand
						receiveCard(card);
					}
					 
					if (card.getStatus().equals("played")) {
						// update stack
						// do whatever may be required--draw cards or be skipped
						onStack = card;
						// draw 4
						if (card.getNum() == -4) {
							drawCard();
							drawCard();
							drawCard();
							drawCard();
						}
						// draw 2
						else if (card.getNum() == -2) {
							drawCard();
							drawCard();
						}
						// skip turn
						else if (card.getNum() == -3) {
							// do nothing...?
						}
						// rotate
						else if (card.getNum() == -5) {
							// again, do nothing
						}
					}
				}
			}
			catch (Exception e) {
				System.out.println("FAILED TO RUN LISTENING THREAD");
				e.printStackTrace();
			}
		}
	} 
 
	public static void main(String[] args) {
		// TESTING--THIS WILL BE DELETED
		Player player = new Player("localhost", 8181, "TESTER", null);
		 
		System.out.println("WILL ATTEMPT TO DRAW 2 CARDS");
		player.drawCard();
		player.drawCard();
 
		System.out.println("HAND CURRENTLY CONTAINS: ");
		ArrayList<Card> temp = player.hand;
		int i = 0;
		for (Card card : temp) {
			System.out.println(temp.get(i).getCol() + " " + temp.get(i).getNum());
			i++;
		}
	 
	}
}
 