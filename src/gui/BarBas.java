package gui;

import game.GameRunner;
import game.Joeur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import lib.GraphPaperLayout;

public class BarBas extends JPanel{
	
	public ArrayList<ArrayList<KComponent>> playerComponents = new ArrayList<ArrayList<KComponent>>();
	
	public final static int INTERVAL = 50;
	public final static double LIGHTENING_FACTOR = .05;
	private Timer timer;
	
	public BarBas() {
		setBackground(Color.CYAN);
		
		setLayout(new GraphPaperLayout(new Dimension(GameRunner.getNumbPlayers(),12)));
		
		for (int i = 0; i < GameRunner.getNumbPlayers(); i++) {
			ArrayList<KComponent> components = new ArrayList<KComponent>();
			components.add(new KComponent(new JLabel(GameRunner.getPlayer(i).getName()), new Rectangle(i,0,1,1)));
			add(components.get(0).getComponent(), components.get(0).getRectangle());
			
			for (int k = 1; k <= 11; k++) {
				components.add(new KComponent(new JLabel(""), new Rectangle(i,k,1,1)));
				components.get(k).getComponent().setFont(new Font("Arial", 1, 16));
				add(components.get(k).getComponent(), components.get(k).getRectangle());
			}
			
			playerComponents.add(components);
		}
		
		timer = new Timer(INTERVAL,
				new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						for (int i = 0; i < playerComponents.size(); i++) {
							updatePlayer(playerComponents.get(i), GameRunner.getPlayer(i));
						}
					}
				});
		timer.start();
		
	}
	
	/*
	public void update(Graphics g) {
		((JLabel)ARGILE.getComponent()).setText("ARGILE: " + GameRunner.currentPlayer.getNumberResourcesType("ARGILE"));
		((JLabel)LAINE.getComponent()).setText("LAINE: " + GameRunner.currentPlayer.getNumberResourcesType("LAINE"));
		((JLabel)ore.getComponent()).setText("Ore: " + GameRunner.currentPlayer.getNumberResourcesType("ORE"));
		((JLabel)BLE.getComponent()).setText("BLE: " + GameRunner.currentPlayer.getNumberResourcesType("BLE"));
		((JLabel)BOIS.getComponent()).setText("BOIS: " + GameRunner.currentPlayer.getNumberResourcesType("BOIS"));
		
		add(ARGILE.getComponent(), ARGILE.getRectangle());
		add(LAINE.getComponent(), LAINE.getRectangle());
		add(ore.getComponent(), ore.getRectangle());
		add(BLE.getComponent(), BLE.getRectangle());
		add(BOIS.getComponent(), BOIS.getRectangle());
		
	}
	*/
	
	public void updatePlayer(ArrayList<KComponent> components, Joeur p){
		((JLabel)components.get(1).getComponent()).setText("ARGILE: " + p.getNumberResourcesType("ARGILE"));
		((JLabel)components.get(2).getComponent()).setText("LAINE: " + p.getNumberResourcesType("LAINE"));
		((JLabel)components.get(3).getComponent()).setText("Ore: " + p.getNumberResourcesType("ORE"));
		((JLabel)components.get(4).getComponent()).setText("BLE: " + p.getNumberResourcesType("BLE"));
		((JLabel)components.get(5).getComponent()).setText("BOIS: " + p.getNumberResourcesType("BOIS"));
		((JLabel)components.get(6).getComponent()).setText("VP: " + p.getVictoryPoints());
		((JLabel)components.get(7).getComponent()).setText("Chevalier: " + p.getDevCardsType("Chevalier"));
		((JLabel)components.get(8).getComponent()).setText("Amoi: " + p.getDevCardsType("Amoi"));
		((JLabel)components.get(9).getComponent()).setText("AnneeAbondonce: " + p.getDevCardsType("AnneeAbondonce"));
		((JLabel)components.get(10).getComponent()).setText("ConstructeurChemin: " + p.getDevCardsType("ConstructeurChemin"));
		((JLabel)components.get(11).getComponent()).setText("PointVictoire: " + p.getDevCardsType("PointVictoire"));
	}
	
}
