import java.io.*;
import java.net.*;
import java.util.*;

public class PlayerHandler {
	ArrayList <Player> players = new ArrayList<Player>();

	// add a Socket
	public void add(Socket sock) {
		players.add(new Player(sock));
		System.out.println("Added to playerhandler; there are " + players.size() + " players");
	}

	// remove a Socket
	public void remove(Socket sock) {
		Iterator<Player> iterator = players.iterator();

		while (iterator.hasNext()) {
			Player temp = iterator.next();
			if (temp.getSocket().equals(sock)) {
				players.remove(temp);
				break;
			}
		}

		System.out.println("Removed from playerhandler; there are " + players.size() + " players");
	}

	// send a card to the opponent
	public void sendCard(Socket sender, Card card) {
		Iterator<Player> iterator = players.iterator();

		while (iterator.hasNext()) {
			Player temp = iterator.next();

			if (!temp.getSocket().equals(sender)) {
				temp.playCard(card); // THIS SOCKET (server-side) sends things BACK TO THE CLIENT
			}
		}
	}

	public void updateOpponent(Socket sender) {
		Iterator<Player> iterator = players.iterator();
		Card card = new Card(-1, 'u');
		card.setStatus("update");

		while (iterator.hasNext()) {
			Player temp = iterator.next();

			if (!temp.getSocket().equals(sender)) {
				temp.playCard(card);
			}
		}
	}

	// deal a card
	public void dealCard(Socket reciever, Card card) {
		Iterator<Player> iterator = players.iterator();

		while (iterator.hasNext()) {
			Player temp = iterator.next();

			if (temp.getSocket().equals(reciever)) {
				temp.playCard(card); // THIS SOCKET (server-side) sends things BACK TO THE CLIENT
			}
		}
	}

	// Checks if any of the other players have UNO
	public void uno (Socket sender, Card card) {
                Iterator<Player> iterator = players.iterator();

		boolean unoFail = false;
		// Checks if any of the other players have uno
                while (iterator.hasNext()) {
                        Player temp = iterator.next();

			// If they have uno
                        if (temp.getHandSize() == 1) {
				
				// Sets status to UNO 
				card.setStatus("Uno!");
				
				// Makes that player draw two cards
			        temp.drawCard();
				temp.drawCard();				
				
                                // Sets unoFail to true
				unoFail = true;
                        }
                }
		
		// If noone else has uno, then the person who pressed the Button has to draw two cards
		if (unoFail == false) {
			// Sender draws two cards
			iterator = players.iterator();
			while (iterator.hasNext()) {
				Player temp = iterator.next();
				if (temp.getSocket().equals(sender)) {
					temp.drawCard();
					temp.drawCard();
					break;
				}
			}
		}

		// Sends Card back to everyone
		sendCard(sender, card); 
	}
	
}
