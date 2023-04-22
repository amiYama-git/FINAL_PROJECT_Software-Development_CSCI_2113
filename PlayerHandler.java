import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;

public class PlayerHandler {
	ArrayList <Player> players = new ArrayList<Player>();
	private ObjectOutputStream objectToPlayer;
	private UnoServer server;

	public PlayerHandler (UnoServer server) {
		this.server = server;
	}

	// add a Socket
	public void add(Socket sock) {
		Player temp = Player.getPlayer(sock);
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
	
}
