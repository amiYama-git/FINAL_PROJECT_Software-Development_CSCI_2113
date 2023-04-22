import java.io.IOException;
import java.io.ObjectOutputStream;
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
			
			try {
				ObjectOutputStream objectToPlayer = new ObjectOutputStream(temp.getSocket().getOutputStream());
				objectToPlayer.writeObject(card);
			}
			catch (Exception e) {}
		}
	}

	// update all players on the number of cards held by the opponent
	public void updateHands(Socket changed, int i) {
		Iterator<Player> iterator = players.iterator();

		while (iterator.hasNext()) {
			Player temp = iterator.next();
			ObjectOutputStream objectToPlayer = null;

			try {
				// write TO the player
				objectToPlayer = new ObjectOutputStream(temp.getSocket().getOutputStream());
			}
			catch (Exception e) { }
			
			if (!temp.getSocket().equals(changed)) {
				Card update = new Card(i, 's');
				update.setStatus("update");

				try {
					objectToPlayer.writeObject(update);
					objectToPlayer.flush();
				} catch (IOException e) {}
			}
		}
	}
	
}
