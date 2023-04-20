/*
 * The GUI
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//break teh array down

public class UnoGUI{

            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            JLayeredPane layered = new JLayeredPane(); //or getLareredPane()?
            ArrayList<JButton> side = new ArrayList<JButton>(); //array of buttons 
            ArrayList<Card> cards = new ArrayList<Card>(); //populated array
            String place; 
            File[] files;

            //1234

    public void UnoGUI(){

        frame.setSize(800,800); //random for now
        layered.setBounds(10,10,800,800);
        //panel.setBounds(10,10,800,800); //.setBounds(int x-coordinate, int y-coordinate, int width, int height);
        frame.add(layered);

        place = System.getProperty("user.dir") + "\\cards"; // directory to the unoimage folder.
        files = new File((place).listFiles()); //adds all images to a file

        //frame.add(panel);
        frame.setTitle("UNO");

        //JPanel panel = new JPanel();
        //JButton button = new JButton();

        for(int i = 0; i < cards.size(); i++){
            System.out.println(cards.get(i).print());
        }

        //------
        /*
        for(int i = 0; i < side.size(); i++){
            panel.add(side.get(i));
        }
        */
        //------
        //panel.setVisible(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //the client/player will pass in a card object,then this class will have to be able to choose the right image to be diplayed, and be layered.
    //if income, then add a layered button on gui.
    //buttons created here and sent in to the client:
        // uno button 
        // draw card button

    //setting things
    
    //button.setOpaque(true);

   
    //button.setBounds(int x-coordinate, int y-coordinate, int width, int height);

    //playcard //if return true, then delete form gui
    //uno button  

    public void GUIadd(Card thing){
        cards.add(thing);//populate array
        System.out.println(cards.size());
        printcards();
    }

    public void printcards(){
            for(int i = 0; i < cards.size(); i++){
            //ImageIcon image = new ImageIcon(##add image icon here);
            ImageIcon image = new ImageIcon(choseImage(cards.get(i).getCol, cards.get(i).getNum));//pass in color and number
            JButton button = new JButton(image);
            button.setBounds (i*30, 10, 100, 100);
            //side.add(button);

            //------
            layered.add(button);
            //------
        }
        /*
        for(int j = 0; j < side.size(); j++){
                panel.add(side.get(j));
            }
        */
        
    }

    public String choseImage(char color, int num){

        for(int i = 0; i < files.length; i++){ //looks into each file 
            String filename = files(i).getName(); //string of file name
            char[] filenameChar = new char[filename.length];// change the string file name to char file name

            for(int k = 0; k < filename.length; i++){ // convert file stirng to file char
                char[k] = filename.chatAt(k);
            }
            
            for()

            if(filenameChar(0).equals(color)){//then red

            }
            else{//special 

            }

        }

    }

    //.dispose() deleter one thing. 
    //player.Onstack(char, int)
    //
    //player.playcard -- asking if you can place it down - returns true if they can 

    //when connect is pressed, create a player object,  
    

    public updateHand(){
        //sends me a array of cards 
    }

    public static void error (String error){
        JFrame errorframe = new JFrame();
        JOptionPane.showMessageDialog(errorframe, error);  
    }

	
}
