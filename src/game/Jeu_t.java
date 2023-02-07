package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import board.Colonie;
import board.PileCarte;
import board.CarteD;
import board.Loca_Chemin;
import board.Localisation;
import board.Peuplement;
import board.Plateau;
import board.Ville;

public class Jeu_t {
	private Plateau P;
	private ArrayList<Joeur> players;
	private Joeur j_act;
	private PileCarte deck;
	private int nbj;
	private int nb_t=0;
	
	public Jeu_t(ArrayList<Joeur> givenPlayers) 
	{
		P=new Plateau();
		if (givenPlayers.size() < 3 || givenPlayers.size() > 5)
			throw new IllegalArgumentException("Game must be played with three or four players");

		ArrayList<String> names = new ArrayList<String>();
		for (Joeur p : givenPlayers) {
			names.add(p.getName());
		}
		for (String s : names) {
			if (Collections.frequency(names, s) > 1)
				throw new IllegalArgumentException("Players must have different names");
		}

		
		
		players = givenPlayers;
		nbj=givenPlayers.size();
		P = new Plateau();
		deck = new PileCarte();
		
		GameRunner.setFirstPlayer();
	}

	void run ()
	{
		j_act=players.get(0);
		
			

				while(!over())
				{
					premier_tour();
					tour();
					
					
					
					
					
					
					
				}
	
	}
	void premier_tour()
	{
		for(int i=0;i<players.size();i++)
		{
			Joeur p=players.get(i);
			System.out.print("Name "+p.getName()+"  ");
			if(p.getIA()==true)
			{
				add_colonie_alea_d(p);
				add_chemain_alea_d(p);
			}
			else {
				
				add_colonie_manu_d(p);
				add_chemain_manu_d(p);
			}
			
		}
		for(int i=players.size()-1;i>0;i--)
		{
			Joeur p=players.get(i);
			System.out.print("Name "+p.getName()+"  ");
			if(p.getIA()==true)
			{
				add_colonie_alea_d(p);
			}
			else {
				
				add_colonie_manu_d(p);
				add_chemain_manu_d(p);
			}
			
		}
		j_act=players.get(0);
	}
	/*
	 * 
	 * Effectuer un tour du jeux 
	 * 
	 * 
	 */
	void tour()
	{
		System.out.println("Au tour de : "+j_act.getName());
		System.out.println("Ses ressources : "+" "+"Argile "+j_act.getNumberResourcesType("ARGILE")
		+" "+"LAINE "+j_act.getNumberResourcesType("LAINE")+" "+"ore "+j_act.getNumberResourcesType("ORE")
		+" "+"BLE "+j_act.getNumberResourcesType("BLE")+" "+"BOIS "+j_act.getNumberResourcesType("BOIS"));
		
		nb_t=nb_t+1;
		int s=roll();
		System.out.println("On jette les dès, score de : "+s);
		if(j_act.getIA()==false)
		{
		if(s==7)
		{
			Scanner sc=new Scanner(System.in);
			System.out.println("Cas resultat 7 ");
			halfCards();
			System.out.println("Choisissez le x ou vous voulez bougez le voleur: ");
			int x= sc.nextInt();
			System.out.println("Choisissez le y ou vous voulez bougez le voleur : ");
			int y=sc.nextInt();
			moveRobber(j_act, new Localisation(x,y) );
			
		}
		else
		{
			cas();
			
			
			}
			
			
			
		}
		
		
		j_act=next_j();
		
		
	}
	void cas()
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Faite votre choix : 0 passez votre tour.");
		System.out.println("                    1 contruire une colonie.");
		System.out.println("                    2 contruire une route.");
		System.out.println("                    3 ameliorer une colonie en ville.");
		System.out.println("                    4 acheter une carte de developpement.");
		System.out.println("                    5 jouer une carte de developpement.");
		System.out.println("Votre choix :");
		int c= sc.nextInt();
		switch(c)
		{
		case 0:
			
			break;
		case 1:
			placer_colonie_m(j_act);
			cas();
			break;
		case 2:
			placer_chemin_m(j_act);
			cas();
			break;
		case 3:
			ameliorer_colonie(j_act);
			cas();
			break;
		case 4:
			achete_carte_dev(j_act);
			cas();
			
			break;
		case 5:
			jouer_carte(j_act);
			cas();
			
			
			break;
		
		
		
		}
	}
	/*
	 * affecter une colonie a un joueur cas de tours normaux
	 * 
	 */
	void placer_colonie_m(Joeur p)
	{
		int i=buySettlement(p);
		if(i==0)
		{
			boolean con=false;
			while(!con)
			{
			Scanner sc = new Scanner(System.in);
			System.out.println("Donner les coordonnées de la colonie à ajouter");
			System.out.print("x :");
			int x=sc.nextInt();
			System.out.print("y :");
			int y=sc.nextInt();
			Localisation l=new Localisation(x,y);
			con=placeSettlement(p,l);
			if(con==false)
			{
				System.out.println("Coordonner non valide veuillez reessayer");
			}
			else
			{
				p.addSettlement();
				p.setVictoryPoints(p.getVictoryPoints() + 1);
			}
			}
		}	
	}
	/*
	 * affecter un chemin a un joueur cas sur des tours normaux
	 * 
	 */
	void placer_chemin_m(Joeur p)
	{
		int i=buySettlement(p);
		if(i==0)
		{
			boolean con=false;
			while(!con)
			{
			Scanner sc = new Scanner(System.in);
			System.out.println("Donner les coordonnées de la colonie à ajouter");
			System.out.print("x :");
			int x=sc.nextInt();
			System.out.print("y :");
			int y=sc.nextInt();
			Localisation l=new Localisation(x,y);
			con=placeSettlement(p,l);
			if(con==false)
			{
				System.out.println("Coordonner non valide veuillez reessayer");
			}
			else
			{
				p.addSettlement();
				p.setVictoryPoints(p.getVictoryPoints() + 1);
			}
			}
			}
		}
	/*
	 * Ameliore une colonie en ville
	 * 
	 */
	void ameliorer_colonie(Joeur p)
	{
		int i=buyCity(p);
		if(i==0)
		{
			boolean con=false;
			while(!con)
			{
			Scanner sc = new Scanner(System.in);
			System.out.println("Donner les coordonnées de la colonie à ameliorer en ville");
			System.out.print("x :");
			int x=sc.nextInt();
			System.out.print("y :");
			int y=sc.nextInt();
			Localisation l=new Localisation(x,y);
			con=placeCity(p,l);
			if(con==false)
			{
				System.out.println("Coordonner non valide veuillez reessayer");
			}
			else
			{
				p.addSettlement();
				p.setVictoryPoints(p.getVictoryPoints() + 1);
			}
			}
			}
		}
		/*
		 * Permet d'acheter une carte de developpement
		 * 
		 */
	void achete_carte_dev(Joeur p)
	{
		int i=buyDevCard(p);
		if(i==0)
		{
			CarteD c=deck.draw();
			if(c!=null)
			{
				p.addDevCard(c);
				System.out.println("La carte pioché est :"+c.getSousT());
			}
			else
			{
				System.out.println("La pile de carte est vide");
				
			}
			
			}
		}
	/*
	 * sert a jouer une carte de developpement 
	 */
	void jouer_carte(Joeur p)
	{
		System.out.println("Les cartes que vous possedez :");
		p.afficher_main();
		System.out.println("Entrer le nom de la carte que vous voulez jouer");
		Scanner sc = new Scanner(System.in);
		String s=sc.nextLine();
		if(p.hasCard(s))
		{
			p.removeCard(s);
			switch(s) {
			case"Chevalier":
				voleur();
				p.decrementNumbChevaliers();
				break;
			case"PointVictoire":
				p.setVictoryPoints(p.getVictoryPoints()+1);
				break;
			}
			
			
		}
	}
	/*
	 * void voleur
	 * apparition du voleur
	 * 
	 * 
	 */
	void voleur()
	{
	Scanner sc=new Scanner(System.in);
	System.out.println("Choisissez le x ou vous voulez bougez le voleur: ");
	int x= sc.nextInt();
	System.out.println("Choisissez le y ou vous voulez bougez le voleur : ");
	int y=sc.nextInt();
	moveRobber(j_act, new Localisation(x,y) );
	}
	
		
		
		
		
		
		
		
	//}
	/*
	 * Ajouter une colonie à un joueur d'une façon manuel
	 * valable que pour le 1er tour 
	 */
	void add_colonie_manu_d(Joeur p)
	{
		boolean con=false;
		System.out.println("Donner les coordonnées de la colonie à ajouter");
		Scanner sc = new Scanner(System.in);
		
		while(con==false)
		{
			System.out.print("x :");
			int x=sc.nextInt();
			//System.out.println("Kechmegh");
			System.out.print("y :");
			int y=sc.nextInt();
			Localisation l=new Localisation(x,y);
			//System.out.println("Kechmegh");
			con=P.placeStructureNoRoad(l,p);
			
			if(con==false)
				System.out.println("Emplacement dèjà occupé ou a moins de 2 cases d un peuplement veuillez introduire un nouvelle emplacement");
			else
			{
				p.addSettlement();
				p.setVictoryPoints(p.getVictoryPoints() + 1);
			}
		}
		
		
		
		
	}
	/*
	 * @param Player p 
	 * Ajoute une colonie à un joueur de façon aléatoire
	 * valable que pour le 1er tour 
	 */
	void add_colonie_alea_d(Joeur p)
	{
		boolean con=false;
		Colonie c;
		while(con==false)
		{
			
				int x=generer_x();
				int y=generer_y();
				System.out.println("Val x : "+x);
				System.out.println("Val y : "+y);
				Localisation l=new Localisation(x,y);
				con=P.placeStructureNoRoad(l,p);
				if(con==true)
				{
					p.addSettlement();
					p.setVictoryPoints(p.getVictoryPoints() + 1);
				}
		}
		
		
		
		
		
	}
	/*
	Permet de placer les routes de façon automatique
	@param Player
	@return boolean
	renvoie vrai si le chemain à bien et bien était placé
	valable que pour le 1er tour 
	*/
	void add_chemain_alea_d(Joeur p)
	{
		boolean con=false;
		Colonie c;
		while(con==false)
		{
			
				int x=generer_x();
				int y=generer_y();
				Random rand = new Random();
				int o = rand.nextInt()%2;
				if (o<0)
				{
					o=o*-1;
				}
				Loca_Chemin l=new Loca_Chemin(x,y,o);
				con=P.placeRoad(l,p);
				if(con==false)
					System.out.println("Emplacement dèjà occupé par un chemin veuillez introduire un nouvelle emplacement");
		}
		
		
		
		
	}
	/*
	 * 	joute un chemin manuellement valable que le 1er tour
	 * valable que pour le 1er tour 
	 * 
	 * 
	 */
	void add_chemain_manu_d(Joeur p)
	{
		
		boolean con=false;
		System.out.println("Donner les coordonnées du chemain à ajouter");
		Scanner sc = new Scanner(System.in);
		
		while(con==false)
		{
			System.out.println("x :");
			int x=sc.nextInt();
			System.out.println("y :");
			int y=sc.nextInt();
			System.out.println("o :");
			int o=sc.nextInt();
			Loca_Chemin l=new Loca_Chemin(x,y,o);
			con=P.placeRoad(l,p);
			if(con==false)
				System.out.println("Emplacement dèjà occupé veuillez introduire un nouvelle emplacement");
		}
	}
	//renvoie true si il y a une colonie à cette localisation
	boolean check_colonie(Localisation l)
	{
		
		Peuplement s=P.getStructure(l);
		if(s.getOwner()!=null)
			return true;
		else
			return false;
		
		
	}
	int generer_x()
	{
		Random rand = new Random();
		int i = rand.nextInt()%3;
		if(i<0)
			i=i*-1;
		i=i+2;
		return i;	
	}
	int generer_y()
	{
		Random rand = new Random();
		int i = rand.nextInt()%4;
		if(i<0)
			i=i*-1;
		i=i+1;
		return i;	
	}
	 int lancer_des()
	{
		
		Random rand = new Random();
		int i = rand.nextInt()%5;
		if(i<0)
			i=i*-1;
		i=i+1;
		System.out.println(i);
		int j = rand.nextInt()%5;
		if(j<0)
			j=j*-1;
		j=j+1;
		i=i+j;
		System.out.println(i);
		return i;
		
	}
	
	
	Joeur next_j()
	{
		int i=players.indexOf(j_act);
		if(i==players.size()-1)
		{
			return players.get(0);
		}
		else 
		{
			i++;
			return players.get(i);
		}
	}
	
	
	public boolean over() {
		return winningPlayer() != null;
	}

	
	public Joeur winningPlayer() {

		Joeur maxVictoryPoints = players.get(0);
		Joeur secondMaxVictoryPoints = players.get(0);

		for (Joeur p : players) {

			if (p.getVictoryPoints() > maxVictoryPoints.getVictoryPoints()) {
				maxVictoryPoints = p;
			}
			else if (p.getVictoryPoints() > secondMaxVictoryPoints.getVictoryPoints()) {
				secondMaxVictoryPoints = p;
			}
		}

		if (maxVictoryPoints.getVictoryPoints() >= 5 && maxVictoryPoints.getVictoryPoints() > secondMaxVictoryPoints.getVictoryPoints()) {
			return maxVictoryPoints;
		}
		else
			return null;
	}

	
	public int roll() {

		// RTD
		int roll = (int)(Math.random() %6 + 1) + (int)(Math.random() %6 + 1);
		//int roll=7;
		if (roll == 7) {
			return roll;
		}
		else {
			// Distribute resources
			P.distributeResources(roll);

			return roll;
		}
	}

	
	public boolean moveRobber(Joeur p, Localisation loc) {

		Localisation prev = P.getRobberLocation();

		if (loc.equals(prev)) {
			return false;
		}

		P.setRobberLocation(loc);
		P.getTile(loc).setRobber(true);
		P.getTile(prev).setRobber(false);

		return true;
	}

	
	public void takeCard(Joeur p, Joeur choice) {

		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < choice.getNumberResourcesType("ARGILE"); i++) {
			res.add("ARGILE");
		}
		for (int i = 0; i < choice.getNumberResourcesType("LAINE"); i++) {
			res.add("LAINE");
		}
		for (int i = 0; i < choice.getNumberResourcesType("ORE"); i++) {
			res.add("ORE");
		}
		for (int i = 0; i < choice.getNumberResourcesType("BLE"); i++) {
			res.add("BLE");
		}
		for (int i = 0; i < choice.getNumberResourcesType("BOIS"); i++) {
			res.add("BOIS");
		}
		
		Collections.shuffle(res);

		if (res.size() <= 0) {
			return;
		}
		String result = res.get(0);

		choice.setNumberResourcesType(result, choice.getNumberResourcesType(result) - 1);

		p.setNumberResourcesType(result, p.getNumberResourcesType(result) + 1);
	}

	public void halfCards() {

		for (Joeur p : players) {

			int cap = 7;
			int numbCards = p.getNumberResourcesType("ARGILE") +
							p.getNumberResourcesType("LAINE") +
							p.getNumberResourcesType("ORE") +
							p.getNumberResourcesType("BLE") +
							p.getNumberResourcesType("BOIS");
			int currentCards = numbCards;

			boolean done = false;

			do {
				currentCards = p.getNumberResourcesType("ARGILE") +
							   p.getNumberResourcesType("LAINE") +
							   p.getNumberResourcesType("ORE") +
							   p.getNumberResourcesType("BLE") +
							   p.getNumberResourcesType("BOIS");

				if (currentCards > cap) {
					int input = 0; //TODO: input
						/* Possible Values:
						 * 0 - BOIS
						 * 1 - ARGILE
						 * 2 - LAINE
						 * 3 - BLE
						 * 4 - ORE
						 */
					cap = numbCards / 2;

					switch (input) {
					case 0:
						p.setNumberResourcesType("BOIS", p.getNumberResourcesType("BOIS") - 1);
						break;
					case 1:
						p.setNumberResourcesType("ARGILE", p.getNumberResourcesType("ARGILE") - 1);
						break;
					case 2:
						p.setNumberResourcesType("LAINE", p.getNumberResourcesType("LAINE") - 1);
						break;
					case 3:
						p.setNumberResourcesType("BLE", p.getNumberResourcesType("BLE") - 1);
						break;
					case 4:
						p.setNumberResourcesType("ORE", p.getNumberResourcesType("ORE") - 1);
						break;
					}
				}
				else {
					done = true;
				}
			} while (!done);
		}
	}
	
	/**
	 * Causes the given player to take all the specified resource from all other players (for Amoi cards)
	 * @param res the resource to take
	 * @param p the player receiving all of that res
	 */
	public void takeAll(String res, Joeur p) {
		
		ArrayList<Joeur> plays = new ArrayList<Joeur>(players);
		plays.remove(p);
		
		for (Joeur player : plays) {
			int tmp = player.getNumberResourcesType(res);
			
			player.setNumberResourcesType(res, 0);
			p.setNumberResourcesType(res, p.getNumberResourcesType(res) + tmp);
		}
	}

	
	public int buyRoad(Joeur p) {

		if (p.getNumberResourcesType("ARGILE") < 1 || p.getNumberResourcesType("BOIS") < 1) {
			return 1;
		}

		// Check Player has not exceeded capacity for object
		if (p.getNumbRoads() >= 15) {
			return 2;
		}

		p.setNumberResourcesType("ARGILE", p.getNumberResourcesType("ARGILE") - 1);
		p.setNumberResourcesType("BOIS", p.getNumberResourcesType("BOIS") - 1);

		//p.setVictoryPoints(p.getVictoryPoints() + 1);  TODO road PointVictoires

		p.addRoadCount();
		return 0;
	}

	/**
	 * Buys Settlement for given Player
	 * @param p the given Player
	 * @return 0=success, 1=insufficient resources, 2=structure limit reached
	 */
	public int buySettlement(Joeur p) {

		// Check Player has sufficient resources
		if (p.getNumberResourcesType("ARGILE") < 1 || p.getNumberResourcesType("BLE") < 1 || p.getNumberResourcesType("LAINE") < 1 || p.getNumberResourcesType("BOIS") < 1) 
		{
			System.out.println("Vous n'avez pas assez de ressource");
			return 1;
		}

		// Check Player has not exceeded capacity for object
		if (p.getNumbSettlements() >= 5) {
			System.out.println("Vous avez dépassez votre nombre limites de ville ");
			return 2;
		}

		p.setNumberResourcesType("ARGILE", p.getNumberResourcesType("ARGILE") - 1);
		p.setNumberResourcesType("BOIS", p.getNumberResourcesType("BOIS") - 1);
		p.setNumberResourcesType("BLE", p.getNumberResourcesType("BLE") - 1);
		p.setNumberResourcesType("LAINE", p.getNumberResourcesType("LAINE") - 1);

		p.setVictoryPoints(p.getVictoryPoints() + 1);
		
		p.addSettlement();
		return 0;
	}

	
	public int buyCity(Joeur p) {

		// Check Player has sufficient resources
		if (p.getNumberResourcesType("BLE") < 2 || p.getNumberResourcesType("ORE") < 3) {
			return 1;
		}

		// Check Player has not exceeded capacity for object
		if (p.getNumbCities() >= 4) {
			return 2;
		}

		p.setNumberResourcesType("BLE", p.getNumberResourcesType("BLE") - 2);
		p.setNumberResourcesType("ORE", p.getNumberResourcesType("ORE") - 3);

		p.setVictoryPoints(p.getVictoryPoints() + 1);

		p.upCity();
		return 0;
	}

	
	public int buyDevCard(Joeur p) {
		
		// Check Player has sufficient resources
		if (p.getNumberResourcesType("ORE") < 1 || p.getNumberResourcesType("LAINE") < 1 || p.getNumberResourcesType("BLE") < 1) {
			System.out.println("Pas assez de ressource");
			return 1;
		}
		if (deck.isEmpty()) {
			System.out.println("Pile Vide");
			return 2;
		}
		p.setNumberResourcesType("ORE", p.getNumberResourcesType("ORE") - 1);
		p.setNumberResourcesType("LAINE", p.getNumberResourcesType("LAINE") - 1);
		p.setNumberResourcesType("BLE", p.getNumberResourcesType("BLE") - 1);
				
		return 0;
	}

	
	public boolean placeRoad(Joeur p, Loca_Chemin loc) {
		return P.placeRoad(loc, p);
	}

	
	public boolean placeSettlement(Joeur p, Localisation loc) 
	{
		return P.placeStructure(loc, p);
	}

	
	public boolean placeCity(Joeur p, Localisation loc) {

		Peuplement s = P.getStructure(loc);

		if (!s.getOwner().equals(p)) {
			return false;
			//TODO: throw error about unowned settlement
		}

		Peuplement c = new Ville(s.getLocation());
		c.setOwner(s.getOwner());
		P.setStructure(loc, c);

		return true;
	}

	
	public Plateau getP(){
		return P;
	}
	
	
	public PileCarte getDeck() {
		return deck;
	}

}
