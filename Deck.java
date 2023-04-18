
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

		// 0
		deck.add(new Card (0, 'r'));
		deck.add(new Card (0, 'y'));
		deck.add(new Card (0, 'g'));
                deck.add(new Card (0, 'b'));

		// 1
		deck.add(new Card (1, 'r'));
                deck.add(new Card (1, 'y'));
                deck.add(new Card (1, 'g'));
                deck.add(new Card (1, 'b'));
                deck.add(new Card (1, 'r'));
                deck.add(new Card (1, 'y'));
                deck.add(new Card (1, 'g'));
                deck.add(new Card (1, 'b'));

		// 2
                deck.add(new Card (2, 'r'));
                deck.add(new Card (2, 'y'));
                deck.add(new Card (2, 'g'));
                deck.add(new Card (2, 'b'));
                deck.add(new Card (2, 'r'));
                deck.add(new Card (2, 'y'));
                deck.add(new Card (2, 'g'));
                deck.add(new Card (2, 'b'));

		// 3
                deck.add(new Card (3, 'r'));
                deck.add(new Card (3, 'y'));
                deck.add(new Card (3, 'g'));
                deck.add(new Card (3, 'b'));
                deck.add(new Card (3, 'r'));
                deck.add(new Card (3, 'y'));
                deck.add(new Card (3, 'g'));
                deck.add(new Card (3, 'b'));

		// 4
                deck.add(new Card (4, 'r'));
                deck.add(new Card (4, 'y'));
                deck.add(new Card (4, 'g'));
                deck.add(new Card (4, 'b'));
                deck.add(new Card (4, 'r'));
                deck.add(new Card (4, 'y'));
                deck.add(new Card (4, 'g'));
                deck.add(new Card (4, 'b'));

		// 5
                deck.add(new Card (5, 'r'));
                deck.add(new Card (5, 'y'));
                deck.add(new Card (5, 'g'));
                deck.add(new Card (5, 'b'));
                deck.add(new Card (5, 'r'));
                deck.add(new Card (5, 'y'));
                deck.add(new Card (5, 'g'));
                deck.add(new Card (5, 'b'));

		// 6 
                deck.add(new Card (6, 'r'));
                deck.add(new Card (6, 'y'));
                deck.add(new Card (6, 'g'));
                deck.add(new Card (6, 'b'));
                deck.add(new Card (6, 'r'));
                deck.add(new Card (6, 'y'));
                deck.add(new Card (6, 'g'));
                deck.add(new Card (6, 'b'));

		// 7 
                deck.add(new Card (7, 'r'));
                deck.add(new Card (7, 'y'));
                deck.add(new Card (7, 'g'));
                deck.add(new Card (7, 'b'));
                deck.add(new Card (7, 'r'));
                deck.add(new Card (7, 'y'));
                deck.add(new Card (7, 'g'));
                deck.add(new Card (7, 'b'));

		// 8 
                deck.add(new Card (8, 'r'));
                deck.add(new Card (8, 'y'));
                deck.add(new Card (8, 'g'));
                deck.add(new Card (8, 'b'));
                deck.add(new Card (8, 'r'));
                deck.add(new Card (8, 'y'));
                deck.add(new Card (8, 'g'));
                deck.add(new Card (8, 'b'));

		// 9 
                deck.add(new Card (9, 'r'));
                deck.add(new Card (9, 'y'));
                deck.add(new Card (9, 'g'));
                deck.add(new Card (9, 'b'));
                deck.add(new Card (9, 'r'));
                deck.add(new Card (9, 'y'));
                deck.add(new Card (9, 'g'));
                deck.add(new Card (9, 'b'));

		// Skip
                deck.add(new Card (-3, 'r'));
                deck.add(new Card (-3, 'y'));
                deck.add(new Card (-3, 'g'));
                deck.add(new Card (-3, 'b'));
                deck.add(new Card (-3, 'r'));
                deck.add(new Card (-3, 'y'));
                deck.add(new Card (-3, 'g'));
                deck.add(new Card (-3, 'b'));

		// Rotate
                deck.add(new Card (-5, 'r'));
                deck.add(new Card (-5, 'y'));
                deck.add(new Card (-5, 'g'));
                deck.add(new Card (-5, 'b'));
                deck.add(new Card (-5, 'r'));
                deck.add(new Card (-5, 'y'));
                deck.add(new Card (-5, 'g'));
                deck.add(new Card (-5, 'b'));

		// Plus Two
                deck.add(new Card (-2, 'r'));
                deck.add(new Card (-2, 'y'));
                deck.add(new Card (-2, 'g'));
                deck.add(new Card (-2, 'b'));
                deck.add(new Card (-2, 'r'));
                deck.add(new Card (-2, 'y'));
                deck.add(new Card (-2, 'g'));
                deck.add(new Card (-2, 'b'));

		// WILD CARD!!!! :-)
		deck.add(new Card (-1, 's'));
                deck.add(new Card (-1, 's'));
                deck.add(new Card (-1, 's'));
                deck.add(new Card (-1, 's'));

		// WILD Plus Four
		deck.add(new Card(-4, 's'));
                deck.add(new Card(-4, 's'));
                deck.add(new Card(-4, 's'));
                deck.add(new Card(-4, 's'));


		// Shuffling the Deck
		shuffle();
        }
}
