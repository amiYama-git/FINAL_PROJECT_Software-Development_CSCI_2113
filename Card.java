public class Card {
	private int number; // 0 - 9 is a normal card; -2 is plus two cards; -4 is plus 4 cards, -1 is change color, -3 skip, -5 rotate
	private char color; // r, y, b, g are normally colored cards; wild card is s (for special)

	// Card Constructor
	public Card (int number, char color) {
		this.number = number;
		this.color = color;
	}
	
	// Number Getter
	public int getNum () {
		return number;
	}

	// Color Getter
	public char getCol () {
		return color;
	}

	// For Testing Purposes
	public String print () {
		String print = "" + number + ", " + color;
		return print;
	}
}
