package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import board.*;


/*
 * The main game class of Settlers of Catan
 */
public class Jeu {

	private Plateau P;
	private ArrayList<Joeur> players;
	private Joeur j_act;
	private PileCarte deck;
	private int nbj;
	private int nb_t=0;
	
	public Jeu(ArrayList<Joeur> givenPlayers) 
	{

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

		Collections.shuffle(givenPlayers);
		
		players = givenPlayers;
		nbj=givenPlayers.size();
		P = new Plateau();
		deck = new PileCarte();
		
		GameRunner.setFirstPlayer();
	}

	void run ()
	{
			Plateau P=new Plateau();

			
				if(nb_t==0)
				{
					this.lancer_des();
					
					
				}
				
				
				
				
				
				
				
				
			
			
			
		
			
		
		
		
		
		
		
		
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
		if(i==players.size())
		{
			return players.get(0);
		}
		else {
			return players.get(i+1);
		}
	}
	/**
	 * Checks if one player has ten or more PointVictoires and more points than any other player
	 * @return whether anyone has one yet
	 */
	
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

		if (maxVictoryPoints.getVictoryPoints() >= 10 && maxVictoryPoints.getVictoryPoints() > secondMaxVictoryPoints.getVictoryPoints()) {
			return maxVictoryPoints;
		}
		else
			return null;
	}

	
	public int roll(Joeur p) {

		// RTD
		int roll = (int)(Math.random() * 6 + 1) + (int)(Math.random() * 6 + 1);

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
	
	
	public void takeAll(String res, Joeur p) {
		
		ArrayList<Joeur> plays = new ArrayList<Joeur>(players);
		plays.remove(p);
		
		for (Joeur player : plays) {
			int tmp = player.getNumberResourcesType(res);
			
			player.setNumberResourcesType(res, 0);
			p.setNumberResourcesType(res, p.getNumberResourcesType(res) + tmp);
		}
	}

	
	
	public boolean playerTrade(Joeur a, Joeur b, ArrayList<String> fromA, ArrayList<String> fromB) {

		if (!a.hasResources(fromA) ||  !b.hasResources(fromB)) {
			return false;
		}

		for (String res : fromA) {
			a.setNumberResourcesType(res, a.getNumberResourcesType(res) - 1);
			b.setNumberResourcesType(res, b.getNumberResourcesType(res) + 1);
		}

		for (String res : fromB) {
			b.setNumberResourcesType(res, b.getNumberResourcesType(res) - 1);
			a.setNumberResourcesType(res, a.getNumberResourcesType(res) + 1);
		}

		return true;
	}

	
	public int npcTrade(Joeur a, String resourceBuying, ArrayList<String> fromA) {
		if (!a.hasResources(fromA))
			return 1;

		//TODO: check if this npc trade is valid (valid ratios, harbors, etc)
		boolean[] ports = a.getPorts();
		ArrayList<Integer> resources = new ArrayList<Integer>();
		resources.add(Collections.frequency(fromA,"ARGILE"));
		resources.add(Collections.frequency(fromA,"LAINE"));
		resources.add(Collections.frequency(fromA,"ORE"));
		resources.add(Collections.frequency(fromA,"BLE"));
		resources.add(Collections.frequency(fromA,"BOIS"));
		
		//int nARGILE = Collections.frequency(fromA,"ARGILE");
		//int toARGILE = Collections.frequency(toA,"ARGILE");
		//int nLAINE = Collections.frequency(fromA,"LAINE");
		//int toLAINE = Collections.frequency(toA,"LAINE");
		//int nOre = Collections.frequency(fromA,"ORE");
		//int toOre = Collections.frequency(toA,"ORE");
		//int nBLE = Collections.frequency(fromA,"BLE");
		//int toBLE = Collections.frequency(toA,"BLE");
		//int nBOIS = Collections.frequency(fromA,"BOIS");
		//int toBOIS = Collections.frequency(toA,"BOIS");
		ArrayList<String> toA = new ArrayList<String>();

		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i) == 0) {}
			else {
				if (ports[i + 1]) {
					if (resources.get(i) % 2 == 0) {
						for (int k = 0; k < resources.get(i) / 2; k++) {
							toA.add(resourceBuying);
						}
					}
					else {
						return 2;
					}
				}
				else if (ports[0]) {
					if (resources.get(i) % 3 == 0) {
						for (int k = 0; k < resources.get(i) / 3; k++) {
							toA.add(resourceBuying);
						}
					}
					else {
						return 2;
					}
				}
				else {
					if (resources.get(i) % 4 == 0) {
						for (int k = 0; k < resources.get(i) / 4; k++) {
							toA.add(resourceBuying);
						}
					}
					else {
						return 2;
					}
				}
			}
		}
		
		
		for (String res : fromA) {
			a.setNumberResourcesType(res, a.getNumberResourcesType(res) - 1);
		}
		
		
		for (String res : toA) {
			a.setNumberResourcesType(res, a.getNumberResourcesType(res) + 1);
		}

		return 0;
	}

	
	public int buyRoad(Joeur p) {

		if (p.getNumberResourcesType("ARGILE") < 1 || p.getNumberResourcesType("BOIS") < 1) {
			return 1;
		}

	
		if (p.getNumbRoads() >= 15) {
			return 2;
		}

		p.setNumberResourcesType("ARGILE", p.getNumberResourcesType("ARGILE") - 1);
		p.setNumberResourcesType("BOIS", p.getNumberResourcesType("BOIS") - 1);

	

		p.addRoadCount();
		return 0;
	}

	
	public int buySettlement(Joeur p) {

		
		if (p.getNumberResourcesType("ARGILE") < 1 || p.getNumberResourcesType("BLE") < 1 || p.getNumberResourcesType("LAINE") < 1 || p.getNumberResourcesType("BOIS") < 1) {
			return 1;
		}

		
		if (p.getNumbSettlements() >= 5) {
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

		
		if (p.getNumberResourcesType("BLE") < 2 || p.getNumberResourcesType("ORE") < 3) {
			return 1;
		}

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
		
		
		if (p.getNumberResourcesType("ORE") < 1 || p.getNumberResourcesType("LAINE") < 1 || p.getNumberResourcesType("BLE") < 1) {
			return 1;
		}
		
		p.setNumberResourcesType("ORE", p.getNumberResourcesType("ORE") - 1);
		p.setNumberResourcesType("LAINE", p.getNumberResourcesType("LAINE") - 1);
		p.setNumberResourcesType("BLE", p.getNumberResourcesType("BLE") - 1);
		
		if (deck.isEmpty()) {
			return 2;
		}
				
		return 0;
	}

	
	public boolean placeRoad(Joeur p, Loca_Chemin loc) {
		return P.placeRoad(loc, p);
	}

	
	public boolean placeSettlement(Joeur p, Localisation loc) {
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
