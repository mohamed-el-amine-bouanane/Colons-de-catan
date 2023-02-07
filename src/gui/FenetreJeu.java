package gui;

import game.*;

import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import lib.GraphPaperLayout;


public class FenetreJeu {
		
	CatanP P;
	BarBas bottom;
	SideBar side;
	public final static int INTERVAL = 20;
	
	
	final static int SCRSIZE = 1000; //TODO specify
	
	
	public FenetreJeu(ArrayList<Joeur> players) 
	{
		P = new CatanP(players);
		bottom = new BarBas();
		
		createAndShowGUI();

		Timer timer = new Timer(INTERVAL,
				new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						// Refresh the P
						P.repaint(); //TODO fix validate
						bottom.repaint();
					}
				});

		timer.start();
	}
	
	private void createAndShowGUI() {
		
		JFrame frame = new JFrame("Catan");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		Dimension d = new Dimension(5,6);

		frame.setSize(SCRSIZE + 500, SCRSIZE);
		//frame.setLayout(new GraphPaperLayout(d));
		Container content = frame.getContentPane();
		content.setLayout(new GraphPaperLayout(d));
		//content.add(P);
		content.add(P,new Rectangle(0,0,4,4));
		
		side = new SideBar(this);
		content.add(side,new Rectangle(4,0,1,4));
		
		content.add(bottom,new Rectangle(0,4,5,2));
		
		
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		P.repaint();
	}
	
	public CatanP getP() {
		return P;
	}
}
