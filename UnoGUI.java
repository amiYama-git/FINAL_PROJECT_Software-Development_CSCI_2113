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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class UnoGUI {

    JFrame startingframe = new JFrame();
    JFrame frame = new JFrame();//game frame
    /*
    JPanel panel = new JPanel();
    JLayeredPane layered = new JLayeredPane(); //or getLareredPane()?
    JPanel stack = new JPanel(new FlowLayout(FlowLayout.CENTER)); //JLable?
    */
    JLayeredPane yourside = new JLayeredPane();
    JPanel stackpanel = new JPanel();
    JLabel addstack = new JLabel();
    JLayeredPane mysidepanel = new JLayeredPane();
    JPanel unobuttonpanel = new JPanel();


    ArrayList<JButton> side = new ArrayList<JButton>(); //array of buttons 
    ArrayList<Card> cards = new ArrayList<Card>(); //populated array
    Boolean remove = false;

    public UnoGUI (){ //starting screen

        JPanel North = new JPanel(new GridLayout(3, 2, 10, 10));
        JPanel Northup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel Northmid = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel Northbottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        startingframe.setSize(380,200);

        JPanel South = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel username = new JLabel("Username: ");
        JLabel IPAdress = new JLabel("IPadress: ");
        JLabel Port = new JLabel("Port Number: ");

        JTextField textName = new JTextField(20);
        JTextField textIPAdress = new JTextField(20);
		textIPAdress.setText("localhost");
        JTextField textPort = new JTextField(20);
		textPort.setText("8181");

        JButton connect = new JButton();
        connect.setText("Connect");
        connect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                UnoScreen playingGUI = new UnoScreen(textIPAdress.getText(), Integer.parseInt(textPort.getText()), textName.getText());
                //player
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
        startingframe.add(South, BorderLayout.SOUTH);

        startingframe.setVisible(true);

        startingframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

	public static void error (String error){
		JFrame errorframe = new JFrame();
		JOptionPane.showMessageDialog(errorframe, error);  
		errorframe.dispose();
	}	

	public static void main(String[] args) {
		UnoGUI gui = new UnoGUI();
	}
}


