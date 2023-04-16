public class Card {
	private int number; // 0 - 9 is a normal card; 
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
}
