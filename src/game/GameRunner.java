package game;

import gui.FenetreJeu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import board.CarteD;

public class GameRunner {
	
	private static Joeur currentPlayer;
	private static int numberPlayers;
	private static int index = 0;
	private static ArrayList<Joeur> players = new ArrayList<Joeur>();
	private static Jeu game;
	private static Joeur winner;
	Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		System.out.println("Bienvenu dans les Colons de Catan");
		System.out.println("Choisissez le nombre de joueurs (3 ou 4)");
		
		ArrayList<Joeur> players2 = new ArrayList<Joeur>();
		
		players2.add(new Joeur("P1",	Color.ORANGE, false));
		players2.add(new Joeur("P2",	Color.BLACK,false));
		players2.add(new Joeur("P3",	Color.RED,false));
		players2.add(new Joeur("P4",	Color.BLUE,false));
						
		numberPlayers = players.size();
		System.out.println("Donner le nombre de joueur 3 ou 4");
		Scanner sc=new Scanner(System.in);
		numberPlayers=sc.nextInt();
		numberPlayers=3;
		if(numberPlayers==3||numberPlayers==4)
		{
			for(int i=0; i<numberPlayers;i++)
			{
				players.add(players2.get(i));
				
		}
		System.out.println("Taper 0 pour jouer le jeu textuellemnt 1 pour graphiquement ");
		int i=sc.nextInt();
		if(i==0)
		{
			Jeu_t G = new Jeu_t(players);
			G.run();
		}
		else if(i==1)
		{
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					FenetreJeu tmp = new FenetreJeu(players);
					game = tmp.getP().getGame();
				}
			});
			}

		else
		{
			System.out.println("Entrer un argument valide");
		}
	}}

	
	
	public static Joeur getCurrentPlayer() {
		return currentPlayer;
	}
	
	public static void nextPlayer() {
		currentPlayer = players.get((index + 1) % 4);
		index = (index + 1) % 4;
	}
	
	public static void prevPlayer() {
		currentPlayer = players.get((index - 1) % 4);
		index = (index - 1) % 4;
	}
	
	public static void setFirstPlayer() {
		currentPlayer = players.get(0);
	}
	
	public static void setWinner(Joeur p) {
		winner = p;
	}
	
	public static Joeur getWinner() {
		return winner;
	}
	
	public static int getNumbPlayers() {
		return numberPlayers;
	}
	
	public static Joeur getPlayer(int i) {
		return players.get(i);
	}
}
