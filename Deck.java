
import java.util.*;

public class Deck {

	// Holds all 108 Cards in Game
	private ArrayList <Card> deck;

	public Deck() {
		addCards();
	}

	        // Draws the Top Card from the Deck. 
        public Card drawCard() {

                // If the Deck is Empty, then it adds all of the cards back into the Deck and ReShuffles
                if (deck.isEmpty() == true) {
                        addCards();
                }
                // Removes a Card from Deck and Returns it
                int size = deck.size();
                Card card = deck.get(size);
                deck.remove(size);
                return card;
        }

	// Shuffles the Deck
	private void shuffle() {
		Collections.shuffle(deck);
	}

        // Adds all 108 Cards to the Deck, then shuffles.
        private void addCards() {
		// Adding the Cards (Pain!)

		// Shuffling the Deck
		shuffle();
        }
}
