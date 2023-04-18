/*
 * The GUI
 */

public class UnoGUI {
	public static void main(String[] args) {
		// For Testing Purposes Feel Free to Remove!!
		Deck deck = new Deck();
		deck.print();
		Card card = deck.drawCard();
		
		for (int i = 0; i < 108; i++) {
			card = deck.drawCard();
		}
		System.out.println(card.print());
		deck.print();
	}	
}
