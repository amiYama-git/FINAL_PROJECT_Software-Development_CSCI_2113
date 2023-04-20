import java.net.*;
import java.util.*;

public class PlayerHandler {
	ArrayList <Player> players = new ArrayList<Player>();

	// add a Socket
	public void add(Socket sock) {
		Player temp = new Player(sock);
		players.add(temp);
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
	}

	// send a card to all players -- used to update onStack
	public void sendCard(Card card) {
		Iterator<Player> iterator = players.iterator();

		while (iterator.hasNext()) {
			Player temp = iterator.next();
			temp.receiveCard(card);
		}

	}

	public void updateHandSize(int i) {
		
	}
}
