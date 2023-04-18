
import java.util.*;

public class Deck {

	// Holds all 108 Cards in Game
	private ArrayList <Card> deck;

	// Deck Constructor :-)
	public Deck() {
		deck = new ArrayList <Card> ();
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
                Card card = deck.get(size-1);
                deck.remove(size-1);
                return card;
        }

	// For Testing Purposes
	public void print() {
		Iterator <Card> it = deck.iterator();

		System.out.println("BOTTOM OF DECK");

		// Iterates through Entire Deck, Printing Each Card. 
		while (it.hasNext()) {
			Card test = it.next();
			System.out.println(test.print());
		}
		
		System.out.println("TOP OF DECK");
	}

	// Shuffles the Deck
	private void shuffle() {
		Collections.shuffle(deck);
	}

        // Adds all 108 Cards to the Deck, then shuffles.
        private void addCards() {
		// Adding the Cards (Pain!)

		deck.add(new Card (0, 'r'));
		deck.add(new Card (0, 'y'));

		// Shuffling the Deck
		shuffle();
        }
}
