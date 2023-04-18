/*
 * The GUI
 */

public class UnoGUI {
	public static void main(String[] args) {
		// For Testing Purposes Feel Free to Remove!!
		Deck deck = new Deck();
		deck.print();
		Card card = deck.drawCard();
		System.out.println(card.print());
		deck.print();
	}	
}
