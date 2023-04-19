import java.io.Serializable;

public class Card implements Serializable {
	private int number; // 0 - 9 is a normal card; -2 is plus two cards; -4 is plus 4 cards, -1 is change color, -3 skip, -5 rotate
	private char color; // r, y, b, g are normally colored cards; wild card is s (for special)
	private String status; 
		// used in communication between client and server; will say "drawn" or "played" or "request"
		// "drawn" will tell the player to add it to their hand; "played" indicates that the OTHER player has played this card and so the card onStack should be updated
		// "request" indicates to the server that it needs to send a card back to the player--equivalent of "draw"; the card's status would be "drawn"
		// "drawn" will be exclusive to the server--it changes cards to that status; "played" is exclusive to the client--it does this in the playCard() method
		// this matters because the player needs to know whether to add the card to the hand or to update onStack--it does not change how the server behaves(?)
		// request is a cheap way of sending a card in order to request a card from the server--because I can't quite see how else to do this?

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

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

	public String getStatus() {
		return status;
	}
}
