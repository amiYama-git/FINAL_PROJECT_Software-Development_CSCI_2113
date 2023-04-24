/*
 * The GUI
 */


import java.awt.Image;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import java.io.*;
import java.io.File;


//break teh array down

public class UnoGUI{
            //uni playing screen gui
            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            JFrame startingframe = new JFrame();
            JLayeredPane layered = new JLayeredPane(); //or getLareredPane()?
            ArrayList<JButton> side = new ArrayList<JButton>(); //array of buttons 
            ArrayList<Card> cards = new ArrayList<Card>(); //populated array
            String place; 
            File[] files;//list of the files
            Player player; 
            Boolean show = true;

    public void UnoGUI(){//starting screen

        UNOplayingGUI playingGUI = new UNOplayingGUI();
        JPanel North = new JPanel(new GridLayout(3, 2, 10, 10));
        JPanel Northup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel Northmid = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel Northbottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        //JFrame startingframe = new JFrame();
        startingframe.setSize(380,200);

        //JPanel North = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //JPanel Middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel South = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel username = new JLabel("Username: ");
        JLabel IPAdress = new JLabel("IPadress: ");
        JLabel Port = new JLabel("Port Number: ");

        JTextField textName = new JTextField(20);
        JTextField textIPAdress = new JTextField(20);
        JTextField textPort = new JTextField(20);

        JButton connect = new JButton();
        connect.setText("Connect");
        connect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //player = new Player(textIPAdress.getText(),Integer.parseInt(textPort.getText()),textName.getText(), playingGUI);
                frame.setVisible(true);
                startingframe.setVisible(false);
            }
        });

        Northup.add(username);
        Northup.add(textName);
        Northmid.add(IPAdress);
        Northmid.add(textIPAdress);
        Northbottom.add(Port);
        Northbottom.add(textPort);
        South.add(connect);

        North.add(Northup);
        North.add(Northmid);
        North.add(Northbottom);

        startingframe.add(North, BorderLayout.NORTH);
        //startingframe.add(Middle, BorderLayout.WEST);
        startingframe.add(South, BorderLayout.SOUTH);

        startingframe.setVisible(true);
        // if uno bitton is clicked, check #numbr of cards oneplays has, then if it is more than one, then send a new card object send ransom card number, and set it false .setstatus(fasle)
        //before passing in i make a new card, set status false and then pass it in to the player.
        //card.setstatus()/string object 

        startingframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public class UNOplayingGUI{//pass in boolean so if we need to set true we can. 
        private UNOplayingGUI(){
            frame.setSize(800,800); //random for now
            layered.setBounds(300,300,800,120);
            frame.add(layered); 

            String place = "./" + "cards"; // directory to the unoimage folder.//System.getProperty(
            //"user.dir" + "/cards"
            System.out.println(place);
            files = new File(place).listFiles(); //adds all images to a file

            frame.setTitle("UNO");
            frame.setVisible(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public void hands(ArrayList<Card> hands){
        for(int i = 0; i < hands.size(); i++){
            cards.add(hands.get(i));
            printcards(hands.get(i));
        }//copy hands
        
    }

    //if its one cards at a time
    /*
    public void GUIadd(Card thing){
        cards.add(thing);//populate array
        System.out.println(cards.size());
        printcards(thing);//one card 
    }
    */

    public void printcards(Card onecard){//add image to card
        //which, 1= hand, 2= stack

        String imagefilename = "./" + "cards/" + choseImage(onecard.getCol(), onecard.getNum());
        System.out.println(imagefilename);

        ImageIcon dabIcon = new ImageIcon(imagefilename);
        Image dabImage = dabIcon.getImage();
        Image modifiedImage = dabImage.getScaledInstance(80, 110, java.awt.Image.SCALE_SMOOTH);
        dabIcon = new ImageIcon(modifiedImage);

        JButton button = new JButton(dabIcon);

        //pass in button and card? to add actionlistner


        for(int i = 0; i < cards.size(); i++){
            button.setBounds (i*30, 10, 80, 110); //30 80 110
        }

        button.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
                char color = onecard.getCol();
                System.out.println(color + " -----");//check
                int num = onecard.getNum();
                System.out.println(num + " -----");//check

                System.out.println(cards.size()); //check

                //System.out.println(player.playCard(num,color));//error?

                if(true){ //player.playCard(num, color) == true  issue is here
                    System.out.println("check check");
                    show = false;
                    for(int i = 0; i < cards.size(); i++){
                        if(onecard == cards.get(i)){
                            cards.remove(i);
                        }
                        System.out.println(cards.size()+ " =======");
                    }
                }
            }
        });

        System.out.println(show + "what?");
                layered.add(button);
                if(show = false){
                    button = null;
                }
                show = true;
    }

    public void deletecard(){//want to move it here

    }

    public String choseImage(char color, int num){

        String col = String.valueOf(color);

        return (col +"_"+ num + ".png");
    }

    public void updateHand(){
        //sends me a array of cards 
    }

    public static void error (String error){
        JFrame errorframe = new JFrame();
        JOptionPane.showMessageDialog(errorframe, error);  
    }

	
}
