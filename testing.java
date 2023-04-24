

import java.util.ArrayList;

public class testing{

    private Card onStack;

    public static void main(String[] args){

    ArrayList<Card> cards = new ArrayList<Card>();


    cards.add(new Card(1, 'r'));
    cards.add(new Card(9, 'b'));
    cards.add(new Card(200, 'y'));
    cards.add(new Card(200, 'g'));
    cards.add(new Card(100, 'r'));//change color
    cards.add(new Card(400, 's'));//+4 and change color

    //onStack = new Card(5, 'r');

    UnoGUI GUI = new UnoGUI();

    GUI.UnoGUI(); //atarashione 

    //when teh cards are sent as a card array.
    GUI.hands(cards);

    /*
    for(int i = 0; i < cards.size(); i++){
            GUI.GUIadd(cards.get(i));
    }
    */

    }

    

}


