package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import board.Chemin;
import board.CarteD;


public class Joeur {

	private final String name;
	private boolean IA;
	private final Color color;
	private HashMap<String, Integer> resources;
	private ArrayList<CarteD> hand;
	private ArrayList<Chemin> roads;
	private int numbChevaliers = 0;
	private int numbSettlements = 2;
	private int victoryPoints = 2;
	private int numbRoads = 2;
	private int numbCities = 0;
	private boolean hasLargestArmy;
	private boolean[] ports = {false, false, false, false, false, false};
					// 0 Port Général 3:1
					// 1 Port spécialisé en Argile 2:1
					// 2 (same) Laine 2:1
					// 3 (same) PIERRE 2:1
					// 4 (same) Blé 2:1
					// 5 (same) Bois 2:1


	
	public Joeur(String n, Color c, boolean tj)
	{

		name = n;
		color = c;
		roads = new ArrayList<Chemin>();

		resources = new HashMap<String, Integer>(5);
		resources.put("ARGILE", 0);
		resources.put("LAINE", 0);
		resources.put("ORE", 0);
		resources.put("BLE", 0);
		resources.put("BOIS", 0);

		hand = new ArrayList<CarteD>();
		IA=tj;
	}

	
	public Joeur(String n, Color c,boolean tj, int ARGILE, int LAINE, int ore, int BLE, int BOIS, int vP) {

		this(n,c,tj);

		setNumberResourcesType("ARGILE", ARGILE);
		setNumberResourcesType("LAINE", LAINE);
		setNumberResourcesType("ORE", ore);
		setNumberResourcesType("BLE", BLE);
		setNumberResourcesType("BOIS", BOIS);
		
		victoryPoints = vP;
	}

	
	public String getName() {
		return name;
	}

	
	public Color getColor() {
		return color;
	}

	
	public int getVictoryPoints() {
		return victoryPoints;
	}
	
	public boolean getIA()
	{
		return this.IA;
	}

	
	public void setVictoryPoints(int vP) {
		victoryPoints = vP;
	}

	
	public int getNumberResourcesType(String str) {
		if (str == null || str.equals("DESERT"))
			return 0;
		return resources.get(str).intValue();
	}

	
	public void setNumberResourcesType(String str, int n) {
		resources.put(str, Integer.valueOf(n));
	}


	public void addDevCard(CarteD dC) {
		hand.add(dC);
		if (dC.getType().equals("PointVictoire")) {
			victoryPoints++;
		}
		if(dC.getType().equals("Chevalier"))
		{
			incrementNumbChevaliers();
		}
	}



	
	public void addRoad(Chemin r){
		roads.add(r);
	}

	
	public ArrayList<Chemin> getRoads(){
		return roads;
	}

	
	public ArrayList<String> getOwnedResources() {

		ArrayList<String> res = new ArrayList<String>();
		if (resources.get("ARGILE").intValue() > 0) {
			res.add("ARGILE");
		}
		if (resources.get("BLE").intValue() > 0) {
			res.add("BLE");
		}
		if (resources.get("LAINE").intValue() > 0) {
			res.add("LAINE");
		}
		if (resources.get("BOIS").intValue() > 0) {
			res.add("BOIS");
		}
		if (resources.get("ORE").intValue() > 0) {
			res.add("ORE");
		}

		return res;
	}

	
	public void incrementNumbChevaliers() {
		numbChevaliers++;
	}
	/*
	 * decrement Chevalier
	 * 
	 */
	public void decrementNumbChevaliers() {
		numbChevaliers--;
	}

	
	public int getNumbChevaliers() {
		return numbChevaliers;
	}

	
	public void setHasLargestArmy(Boolean b) {
		if (hasLargestArmy == true && b == false)
			victoryPoints--;
		else if (hasLargestArmy == false && b == true)
			victoryPoints++;
		hasLargestArmy = b;
	}

	
	public boolean hasLargestArmy() 
	{
		return hasLargestArmy;
	}

	public boolean hasResources(ArrayList<String> res) {
		int LAINE = 0,
			ore = 0,
			BOIS = 0,
			ARGILE = 0,
			BLE = 0;

		for (String s : res) {
			if (res.equals("LAINE"))
				LAINE++;
			else if (res.equals("ORE"))
				ore++;
			else if (res.equals("BOIS"))
				BOIS++;
			else if (res.equals("ARGILE"))
				ARGILE++;
			else if (res.equals("BLE"))
				BLE++;
		}

		if (LAINE > resources.get("LAINE") || ore > resources.get("ORE") || BOIS > resources.get("BOIS") || ARGILE > resources.get("ARGILE") || BLE > resources.get("BLE"))
			return false;
		else
			return true;
	}

	public boolean hasCard(String str) {

		for (CarteD dev : hand) {
			if (dev.getSousT() == str || dev.getType() == str)
				return true;
		}

		return false;
	}

	public void removeCard(String str) {
		for (CarteD dC : hand) {
			if (dC.getSousT() == str || dC.getType() == str) {
				hand.remove(dC);
				break;
			}
		}
	}
	public void afficher_main()
	{
		for(CarteD d : hand)
		{
			System.out.print(d.getType());
			if(d.getType().equals("Progre"));
			{
				System.out.print(d.getSousT());
			}
		}
	}
	
	public void addPort(int portTag) {
		ports[portTag] = true;
	}

	public boolean[] getPorts() {
		return ports;
	}

	
	public String toString() {
		return name;
	}

	
	public int getNumbSettlements() {
		return numbSettlements;
	}

	
	public int getNumbCities() {
		return numbCities;
	}

	/**
	 * Getter for numbRoads
	 * @return int number of roads
	 */
	public int getNumbRoads() {
		return numbRoads;
	}
	
	/**
	 * Adds 1 to numbSettlements
	 */
	public void addSettlement() 
	{
		numbSettlements++;
	}
	
	/**
	 * Adds 1 to numbCities
	 */
	public void upCity()
	{
		numbSettlements--;
		numbCities++;
	}
	
	/**
	 * Adds 1 to numbRoads
	 */
	public void addRoadCount() {
		numbRoads++;
	}
	
	/**
	 * Adds one to specified resource
	 * @param str the resource type to increment
	 */
	public void giveResourceType(String str) {
		if (str == null || str == "DESERT") {
			return;
		}
		resources.put(str, resources.get(str) + 1);
	}
		
	/**
	 * Removes all resources in given list from this player
	 * @param rez list of resources to be removed
	 */
	public void removeResources(ArrayList<String> rez) {
		for (String s: rez) {
			//System.out.println("Removed " + s);
			setNumberResourcesType(s, getNumberResourcesType(s) - 1);
		}
	}
	
	/**
	 * Adds all resources in given list to this player
	 * @param rez list of resources to be added
	 */
	public void addResources(ArrayList<String> rez) {
		for (String s: rez) {
			//System.out.println("Removed " + s);
			setNumberResourcesType(s, getNumberResourcesType(s) + 1);
		}
	}
	
	/**
	 * Gets total amount of resources this player has
	 * @return int total resources
	 */
	public int getTotalResources() {
		return getNumberResourcesType("ARGILE") +
		getNumberResourcesType("LAINE") +
		getNumberResourcesType("ORE") +
		getNumberResourcesType("BLE") +
		getNumberResourcesType("BOIS");
	}
	
	/**
	 * Returns number of dev cards of given type or SousT
	 * @param str the card type or SousT
	 * @return the number owned by this Player
	 */
	public int getDevCardsType(String str) {
		int count = 0;
		for (CarteD dC : hand) {
			if (dC.getType().equals(str) || dC.getSousT() != null && dC.getSousT().equals(str))
				count++;
		}
		
		return count;
	}
}
