

public class Deck {

	// Holds all 108 Cards in Game - has
	private ArrayList <Card> deck;

	public Deck() {
		addCards();
	}

	private addCards() {

	}

	public Card drawCard() {
		if (deck.size == 0) {
			addCards();
		}
		// Sends Card to the Server
	}
}
